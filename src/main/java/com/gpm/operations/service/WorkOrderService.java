package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.service.dto.WorkOrderDTO;
import com.gpm.operations.service.mapper.WorkOrderMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkOrder}.
 */
@Service
@Transactional
public class WorkOrderService {

    private final Logger log = LoggerFactory.getLogger(WorkOrderService.class);

    private final WorkOrderRepository workOrderRepository;

    private final WorkOrderMapper workOrderMapper;

    public WorkOrderService(WorkOrderRepository workOrderRepository, WorkOrderMapper workOrderMapper) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderMapper = workOrderMapper;
    }

    /**
     * Save a workOrder.
     *
     * @param workOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkOrderDTO save(WorkOrderDTO workOrderDTO) {
        log.debug("Request to save WorkOrder : {}", workOrderDTO);

        // Un nouveau WorkOrder démarre toujours avec le statut "Creation",
        // quelle que soit la valeur envoyée par le client.
        workOrderDTO.setStatut(StatutWO.Creation);

        WorkOrder workOrder = workOrderMapper.toEntity(workOrderDTO);
        workOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toDto(workOrder);
    }

    /**
     * Update a workOrder.
     *
     * @param workOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkOrderDTO update(WorkOrderDTO workOrderDTO) {
        log.debug("Request to update WorkOrder : {}", workOrderDTO);

        // Le statut ne peut changer que via /work-orders/{id}/transition/{event}
        StatutWO currentStatut = workOrderRepository
            .findById(workOrderDTO.getId())
            .map(WorkOrder::getStatut)
            .orElseThrow(() -> new IllegalArgumentException("WorkOrder not found: " + workOrderDTO.getId()));
        workOrderDTO.setStatut(currentStatut);

        WorkOrder workOrder = workOrderMapper.toEntity(workOrderDTO);
        workOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toDto(workOrder);
    }

    /**
     * Partially update a workOrder.
     *
     * @param workOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkOrderDTO> partialUpdate(WorkOrderDTO workOrderDTO) {
        log.debug("Request to partially update WorkOrder : {}", workOrderDTO);

// Interdit de modifier le statut par PATCH (null = ignoré par le mapper)
        workOrderDTO.setStatut(null);

        return workOrderRepository
            .findById(workOrderDTO.getId())
            .map(existingWorkOrder -> {
                workOrderMapper.partialUpdate(existingWorkOrder, workOrderDTO);

                return existingWorkOrder;
            })
            .map(workOrderRepository::save)
            .map(workOrderMapper::toDto);
    }

    /**
     * Get all the workOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkOrders");
        return workOrderRepository.findAll(pageable).map(workOrderMapper::toDto);
    }

    /**
     * Get all the workOrders, optionnellement filtrés par statut, client et recherche texte.
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
        return workOrderRepository.findAllWithFilters(statut, clientId, term, ids, pageable).map(workOrderMapper::toDto);
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
        return workOrderRepository.findById(id).map(workOrderMapper::toDto);
    }

    /**
     * Delete the workOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrder : {}", id);
        workOrderRepository.deleteById(id);
    }
}
