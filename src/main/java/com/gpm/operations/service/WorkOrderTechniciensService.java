package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.WorkOrderTechniciens;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.repository.WorkOrderTechniciensRepository;
import com.gpm.operations.service.dto.TechnicienConflictDTO;
import com.gpm.operations.service.dto.WorkOrderTechniciensDTO;
import com.gpm.operations.service.mapper.WorkOrderTechniciensMapper;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing {@link WorkOrderTechniciens}.
 */
@Service
@Transactional
public class WorkOrderTechniciensService {

    private final Logger log = LoggerFactory.getLogger(WorkOrderTechniciensService.class);

    private final WorkOrderTechniciensRepository workOrderTechniciensRepository;

    private final WorkOrderTechniciensMapper workOrderTechniciensMapper;

    private final WorkOrderRepository workOrderRepository;

    public WorkOrderTechniciensService(
        WorkOrderTechniciensRepository workOrderTechniciensRepository,
        WorkOrderTechniciensMapper workOrderTechniciensMapper,
        WorkOrderRepository workOrderRepository
    ) {
        this.workOrderTechniciensRepository = workOrderTechniciensRepository;
        this.workOrderTechniciensMapper = workOrderTechniciensMapper;
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * Save a workOrderTechniciens.
     *
     * @param workOrderTechniciensDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkOrderTechniciensDTO save(WorkOrderTechniciensDTO workOrderTechniciensDTO) {
        log.debug("Request to save WorkOrderTechniciens : {}", workOrderTechniciensDTO);
        WorkOrderTechniciens workOrderTechniciens = workOrderTechniciensMapper.toEntity(workOrderTechniciensDTO);
        workOrderTechniciens = workOrderTechniciensRepository.save(workOrderTechniciens);
        return workOrderTechniciensMapper.toDto(workOrderTechniciens);
    }

    /**
     * Partially update a workOrderTechniciens.
     *
     * @param workOrderTechniciensDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkOrderTechniciensDTO> partialUpdate(WorkOrderTechniciensDTO workOrderTechniciensDTO) {
        log.debug("Request to partially update WorkOrderTechniciens : {}", workOrderTechniciensDTO);

        return workOrderTechniciensRepository
            .findById(workOrderTechniciensDTO.getId())
            .map(existingWorkOrderTechniciens -> {
                workOrderTechniciensMapper.partialUpdate(existingWorkOrderTechniciens, workOrderTechniciensDTO);

                return existingWorkOrderTechniciens;
            })
            .map(workOrderTechniciensRepository::save)
            .map(workOrderTechniciensMapper::toDto);
    }

    /**
     * Get all the workOrderTechniciens.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkOrderTechniciensDTO> findAll() {
        log.debug("Request to get all WorkOrderTechniciens");
        return workOrderTechniciensRepository
            .findAll()
            .stream()
            .map(workOrderTechniciensMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one workOrderTechniciens by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkOrderTechniciensDTO> findOne(Long id) {
        log.debug("Request to get WorkOrderTechniciens : {}", id);
        return workOrderTechniciensRepository.findById(id).map(workOrderTechniciensMapper::toDto);
    }

    /**
     * Get all the workOrderTechniciens linked to a given work order.
     *
     * @param workOrderId the id of the work order.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkOrderTechniciensDTO> findByWorkOrderId(Long workOrderId) {
        log.debug("Request to get WorkOrderTechniciens by workOrder : {}", workOrderId);
        return workOrderTechniciensRepository
            .findByWorkOrderId(workOrderId)
            .stream()
            .map(workOrderTechniciensMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Vérifie si les techniciens donnés sont déjà affectés à un autre WorkOrder
     * dont la mission n'est pas encore terminée (dateHeureFinPrev dans le futur).
     *
     * @param contactSocieteIds les ids des techniciens à vérifier.
     * @param excludeWorkOrderId le work order courant à exclure (mode édition), peut être null.
     * @return la liste des conflits détectés (vide si tous disponibles).
     */
    @Transactional(readOnly = true)
    public List<TechnicienConflictDTO> findConflicts(List<Long> contactSocieteIds, Long excludeWorkOrderId) {
        log.debug("Request to check technicien disponibilité : {}, exclude={}", contactSocieteIds, excludeWorkOrderId);

        if (contactSocieteIds == null || contactSocieteIds.isEmpty()) {
            return List.of();
        }

        List<WorkOrderTechniciens> links = workOrderTechniciensRepository.findByContactSocieteIdIn(contactSocieteIds);

        if (links.isEmpty()) {
            return List.of();
        }

        List<Long> workOrderIds = links.stream().map(WorkOrderTechniciens::getWorkOrderId).distinct().collect(Collectors.toList());

        Map<Long, WorkOrder> workOrdersById = workOrderRepository
            .findAllById(workOrderIds)
            .stream()
            .collect(Collectors.toMap(WorkOrder::getId, wo -> wo));

        ZonedDateTime now = ZonedDateTime.now();
        List<TechnicienConflictDTO> conflicts = new LinkedList<>();

        for (WorkOrderTechniciens link : links) {
            WorkOrder wo = workOrdersById.get(link.getWorkOrderId());

            if (wo == null || wo.getDateHeureFinPrev() == null) {
                continue;
            }
            // Ignore le work order courant (mode édition)
            if (excludeWorkOrderId != null && excludeWorkOrderId.equals(wo.getId())) {
                continue;
            }
            // Mission pas encore terminée => conflit
            if (wo.getDateHeureFinPrev().isAfter(now)) {
                conflicts.add(
                    new TechnicienConflictDTO(
                        link.getContactSocieteId(),
                        wo.getId(),
                        wo.getNumFicheIntervention(),
                        wo.getIdentifiantUnique(),
                        wo.getDateHeureFinPrev()
                    )
                );
            }
        }

        return conflicts;
    }

    /**
     * Replace the full list of techniciens linked to a work order
     * (deletes the existing links, then re-creates them from the given contact ids).
     *
     * @param workOrderId the id of the work order.
     * @param contactSocieteIds the ids of the selected contacts (role TECHNIQUE).
     * @return the persisted entities.
     */
    public List<WorkOrderTechniciensDTO> replaceForWorkOrder(Long workOrderId, List<Long> contactSocieteIds) {
        log.debug("Request to replace WorkOrderTechniciens for workOrder : {}, {}", workOrderId, contactSocieteIds);

        workOrderTechniciensRepository.deleteByWorkOrderId(workOrderId);

        List<WorkOrderTechniciens> entities = contactSocieteIds
            .stream()
            .map(contactId -> new WorkOrderTechniciens().workOrderId(workOrderId).contactSocieteId(contactId))
            .collect(Collectors.toCollection(LinkedList::new));

        return workOrderTechniciensRepository
            .saveAll(entities)
            .stream()
            .map(workOrderTechniciensMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Delete the workOrderTechniciens by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrderTechniciens : {}", id);
        workOrderTechniciensRepository.deleteById(id);
    }
}
