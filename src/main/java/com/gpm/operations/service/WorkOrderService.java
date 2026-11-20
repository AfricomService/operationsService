package com.gpm.operations.service;

import com.gpm.operations.domain.AclSid;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.AclPermission;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.security.AuthoritiesConstants;
import com.gpm.operations.security.SecurityUtils;
import com.gpm.operations.service.dto.WorkOrderDTO;
import com.gpm.operations.service.mapper.WorkOrderMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service Implementation for managing {@link WorkOrder}.
 */
@Service
@Transactional
public class WorkOrderService {

    private static final String OBJECT_TYPE = "WORKORDER"; // matches acl_entry.object_type

    private final Logger log = LoggerFactory.getLogger(WorkOrderService.class);

    private final WorkOrderRepository workOrderRepository;

    private final WorkOrderMapper workOrderMapper;

    private final AclUtilService aclUtilService;

    public WorkOrderService(WorkOrderRepository workOrderRepository, WorkOrderMapper workOrderMapper, AclUtilService aclUtilService) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderMapper = workOrderMapper;
        this.aclUtilService = aclUtilService;
    }

    /**
     * Save a workOrder.
     *
     * @param workOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkOrderDTO save(WorkOrderDTO workOrderDTO) {
        log.debug("Request to save WorkOrder : {}", workOrderDTO);
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));

        // Un nouveau WorkOrder démarre toujours avec le statut "Creation",
        // quelle que soit la valeur envoyée par le client.
        workOrderDTO.setStatut(StatutWO.Creation);

        WorkOrder workOrder = workOrderMapper.toEntity(workOrderDTO);
        workOrder = workOrderRepository.save(workOrder);

        // 1. Creator: READ + WRITE
        aclUtilService.grantOwner(OBJECT_TYPE, workOrder.getId(), currentLogin.toLowerCase());

        // 2. Valideur: READ
        if (StringUtils.hasText(workOrder.getValideurUserLogin())) {
            aclUtilService.grantToUser(OBJECT_TYPE, workOrder.getId(), workOrder.getValideurUserLogin().toLowerCase(), AclPermission.READ);
        }

        return withPermissions(workOrderMapper.toDto(workOrder));
    }

    /**
     * Update a workOrder.
     *
     * @param workOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkOrderDTO update(WorkOrderDTO workOrderDTO) {
        log.debug("Request to update WorkOrder : {}", workOrderDTO);
        assertWrite(workOrderDTO.getId()); // << ACL

        // Le statut ne peut changer que via /work-orders/{id}/transition/{event}
        StatutWO currentStatut = workOrderRepository
            .findById(workOrderDTO.getId())
            .map(WorkOrder::getStatut)
            .orElseThrow(() -> new IllegalArgumentException("WorkOrder not found: " + workOrderDTO.getId()));
        workOrderDTO.setStatut(currentStatut);

        WorkOrder workOrder = workOrderMapper.toEntity(workOrderDTO);
        workOrder = workOrderRepository.save(workOrder);
        return withPermissions(workOrderMapper.toDto(workOrder));
    }

    /**
     * Partially update a workOrder.
     *
     * @param workOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkOrderDTO> partialUpdate(WorkOrderDTO workOrderDTO) {
        log.debug("Request to partially update WorkOrder : {}", workOrderDTO);
        assertWrite(workOrderDTO.getId()); // << ACL

        // Interdit de modifier le statut par PATCH (null = ignoré par le mapper)
        workOrderDTO.setStatut(null);

        return workOrderRepository
            .findById(workOrderDTO.getId())
            .map(existingWorkOrder -> {
                workOrderMapper.partialUpdate(existingWorkOrder, workOrderDTO);

                return existingWorkOrder;
            })
            .map(workOrderRepository::save)
            .map(workOrderMapper::toDto)
            .map(this::withPermissions);
    }

    /**
     * Get all the workOrders (ACL filtered).
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkOrders");

        // << ACL : bypass admin
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return workOrderRepository.findAll(pageable).map(workOrderMapper::toDto).map(this::withPermissions);
        }

        List<Long> sidIds = currentSidIds();
        if (sidIds.isEmpty()) {
            return Page.empty(pageable);
        }
        return workOrderRepository.findAllAccessible(sidIds, pageable).map(workOrderMapper::toDto).map(this::withPermissions);
    }

    /**
     * Get all the workOrders, optionnellement filtrés par statut, client et recherche texte.
     * Seuls les WorkOrders accessibles à l'utilisateur courant sont retournés (sauf ADMIN).
     *
     * @param pageable   the pagination information.
     * @param statut     le statut à filtrer ; si {@code null}, ce filtre est ignoré.
     * @param clientId   le client à filtrer ; si {@code null}, ce filtre est ignoré.
     * @param search     texte recherché dans l'identifiant unique ; vide/null = ignoré.
     * @param affaireIds ids d'affaires (résolus côté projectservice) à inclure dans la recherche.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkOrderDTO> findAllByStatut(
        Pageable pageable,
        StatutWO statut,
        Long clientId,
        String search,
        List<Long> affaireIds
    ) {
        log.debug("Request to get all WorkOrders with statut : {}, clientId : {}, search : {}", statut, clientId, search);
        String term = search == null ? "" : search.trim();
        List<Long> ids = (affaireIds == null || affaireIds.isEmpty()) ? List.of(-1L) : affaireIds;

        // << ACL : bypass admin
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return workOrderRepository
                .findAllWithFilters(statut, clientId, term, ids, pageable)
                .map(workOrderMapper::toDto)
                .map(this::withPermissions);
        }

        // << ACL : seulement les work orders accessibles (filtrage en base, pagination préservée)
        List<Long> sidIds = currentSidIds();
        if (sidIds.isEmpty()) {
            return Page.empty(pageable);
        }
        return workOrderRepository
            .findAllWithFiltersAccessible(statut, clientId, term, ids, sidIds, pageable)
            .map(workOrderMapper::toDto)
            .map(this::withPermissions);
    }

    /**
     * Get one workOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkOrderDTO> findOne(Long id) {
        log.debug("Request to get WorkOrder : {}", id);
        return workOrderRepository.findById(id).map(workOrderMapper::toDto).map(this::withPermissions);
    }

    /**
     * Delete the workOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrder : {}", id);
        assertWrite(id); // << ACL
        workOrderRepository.deleteById(id);
        aclUtilService.deleteAcl(OBJECT_TYPE, id);
    }

    /**
     * Must be called before a status transition: ADMIN, or WRITE access on the work order.
     *
     * @param id the id of the workOrder.
     */
    @Transactional(readOnly = true)
    public void assertCanChangeStatut(Long id) {
        boolean privileged = SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN);
        if (!privileged && !aclUtilService.canWrite(OBJECT_TYPE, id)) {
            throw new AccessDeniedException("Pas d'accès en écriture au work order " + id);
        }
    }

    private WorkOrderDTO withPermissions(WorkOrderDTO dto) {
        dto.setCanRead(aclUtilService.canRead(OBJECT_TYPE, dto.getId()));
        dto.setCanWrite(aclUtilService.canWrite(OBJECT_TYPE, dto.getId()));
        return dto;
    }

    private void assertWrite(Long id) {
        if (!aclUtilService.canWrite(OBJECT_TYPE, id)) {
            throw new AccessDeniedException("Pas d'accès en écriture au work order " + id);
        }
    }

    private List<Long> currentSidIds() {
        return aclUtilService.getCurrentUserSids().stream().map(AclSid::getId).collect(Collectors.toList());
    }
}
