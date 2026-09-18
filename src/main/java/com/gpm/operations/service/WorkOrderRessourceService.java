package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.WorkOrderRessource;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.repository.WorkOrderRessourceRepository;
import com.gpm.operations.service.dto.RessourceConflictDTO;
import com.gpm.operations.service.dto.WorkOrderRessourceDTO;
import com.gpm.operations.service.mapper.WorkOrderRessourceMapper;
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
 * Service Implementation for managing {@link WorkOrderRessource}.
 */
@Service
@Transactional
public class WorkOrderRessourceService {

    private final Logger log = LoggerFactory.getLogger(WorkOrderRessourceService.class);

    private final WorkOrderRessourceRepository workOrderRessourceRepository;

    private final WorkOrderRessourceMapper workOrderRessourceMapper;

    private final WorkOrderRepository workOrderRepository;

    public WorkOrderRessourceService(
        WorkOrderRessourceRepository workOrderRessourceRepository,
        WorkOrderRessourceMapper workOrderRessourceMapper,
        WorkOrderRepository workOrderRepository
    ) {
        this.workOrderRessourceRepository = workOrderRessourceRepository;
        this.workOrderRessourceMapper = workOrderRessourceMapper;
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * Save a workOrderRessource.
     *
     * @param workOrderRessourceDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkOrderRessourceDTO save(WorkOrderRessourceDTO workOrderRessourceDTO) {
        log.debug("Request to save WorkOrderRessource : {}", workOrderRessourceDTO);
        WorkOrderRessource workOrderRessource = workOrderRessourceMapper.toEntity(workOrderRessourceDTO);
        workOrderRessource = workOrderRessourceRepository.save(workOrderRessource);
        return workOrderRessourceMapper.toDto(workOrderRessource);
    }

    /**
     * Partially update a workOrderRessource.
     *
     * @param workOrderRessourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkOrderRessourceDTO> partialUpdate(WorkOrderRessourceDTO workOrderRessourceDTO) {
        log.debug("Request to partially update WorkOrderRessource : {}", workOrderRessourceDTO);

        return workOrderRessourceRepository
            .findById(workOrderRessourceDTO.getId())
            .map(existingWorkOrderRessource -> {
                workOrderRessourceMapper.partialUpdate(existingWorkOrderRessource, workOrderRessourceDTO);

                return existingWorkOrderRessource;
            })
            .map(workOrderRessourceRepository::save)
            .map(workOrderRessourceMapper::toDto);
    }

    /**
     * Get all the workOrderRessources.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkOrderRessourceDTO> findAll() {
        log.debug("Request to get all WorkOrderRessources");
        return workOrderRessourceRepository
            .findAll()
            .stream()
            .map(workOrderRessourceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one workOrderRessource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkOrderRessourceDTO> findOne(Long id) {
        log.debug("Request to get WorkOrderRessource : {}", id);
        return workOrderRessourceRepository.findById(id).map(workOrderRessourceMapper::toDto);
    }

    /**
     * Get all the workOrderRessources linked to a given work order.
     *
     * @param workOrderId the id of the work order.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkOrderRessourceDTO> findByWorkOrderId(Long workOrderId) {
        log.debug("Request to get WorkOrderRessources by workOrder : {}", workOrderId);
        return workOrderRessourceRepository
            .findByWorkOrderId(workOrderId)
            .stream()
            .map(workOrderRessourceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Vérifie si les ressources données sont déjà affectées à un autre WorkOrder
     * dont la mission n'est pas encore terminée (dateHeureFinPrev dans le futur).
     *
     * @param ressourceIds les ids des ressources à vérifier.
     * @param excludeWorkOrderId le work order courant à exclure (mode édition), peut être null.
     * @return la liste des conflits détectés (vide si toutes disponibles).
     */
    @Transactional(readOnly = true)
    public List<RessourceConflictDTO> findConflicts(List<Long> ressourceIds, Long excludeWorkOrderId) {
        log.debug("Request to check ressource disponibilité : {}, exclude={}", ressourceIds, excludeWorkOrderId);

        if (ressourceIds == null || ressourceIds.isEmpty()) {
            return List.of();
        }

        List<WorkOrderRessource> links = workOrderRessourceRepository.findByRessourceIdIn(ressourceIds);

        if (links.isEmpty()) {
            return List.of();
        }

        List<Long> workOrderIds = links.stream().map(WorkOrderRessource::getWorkOrderId).distinct().collect(Collectors.toList());

        Map<Long, WorkOrder> workOrdersById = workOrderRepository
            .findAllById(workOrderIds)
            .stream()
            .collect(Collectors.toMap(WorkOrder::getId, wo -> wo));

        ZonedDateTime now = ZonedDateTime.now();
        List<RessourceConflictDTO> conflicts = new LinkedList<>();

        for (WorkOrderRessource link : links) {
            WorkOrder wo = workOrdersById.get(link.getWorkOrderId());

            if (wo == null || wo.getDateHeureFinPrev() == null) {
                continue;
            }
            if (excludeWorkOrderId != null && excludeWorkOrderId.equals(wo.getId())) {
                continue;
            }
            if (wo.getDateHeureFinPrev().isAfter(now)) {
                conflicts.add(
                    new RessourceConflictDTO(
                        link.getRessourceId(),
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
     * Replace the full list of ressources linked to a work order
     * (deletes the existing links, then re-creates them from the given ressource ids).
     *
     * @param workOrderId the id of the work order.
     * @param ressourceIds the ids of the selected ressources.
     * @return the persisted entities.
     */
    public List<WorkOrderRessourceDTO> replaceForWorkOrder(Long workOrderId, List<Long> ressourceIds) {
        log.debug("Request to replace WorkOrderRessources for workOrder : {}, {}", workOrderId, ressourceIds);

        workOrderRessourceRepository.deleteByWorkOrderId(workOrderId);

        List<WorkOrderRessource> entities = ressourceIds
            .stream()
            .map(ressourceId -> new WorkOrderRessource().workOrderId(workOrderId).ressourceId(ressourceId))
            .collect(Collectors.toCollection(LinkedList::new));

        return workOrderRessourceRepository
            .saveAll(entities)
            .stream()
            .map(workOrderRessourceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Delete the workOrderRessource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrderRessource : {}", id);
        workOrderRessourceRepository.deleteById(id);
    }
}
