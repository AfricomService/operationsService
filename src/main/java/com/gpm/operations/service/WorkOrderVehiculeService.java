package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.WorkOrderVehicule;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.repository.WorkOrderVehiculeRepository;
import com.gpm.operations.service.dto.VehiculeConflictDTO;
import com.gpm.operations.service.dto.WorkOrderVehiculeDTO;
import com.gpm.operations.service.mapper.WorkOrderVehiculeMapper;
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
 * Service Implementation for managing {@link WorkOrderVehicule}.
 */
@Service
@Transactional
public class WorkOrderVehiculeService {

    private final Logger log = LoggerFactory.getLogger(WorkOrderVehiculeService.class);

    private final WorkOrderVehiculeRepository workOrderVehiculeRepository;

    private final WorkOrderVehiculeMapper workOrderVehiculeMapper;

    private final WorkOrderRepository workOrderRepository;

    public WorkOrderVehiculeService(
        WorkOrderVehiculeRepository workOrderVehiculeRepository,
        WorkOrderVehiculeMapper workOrderVehiculeMapper,
        WorkOrderRepository workOrderRepository
    ) {
        this.workOrderVehiculeRepository = workOrderVehiculeRepository;
        this.workOrderVehiculeMapper = workOrderVehiculeMapper;
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * Save a workOrderVehicule.
     *
     * @param workOrderVehiculeDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkOrderVehiculeDTO save(WorkOrderVehiculeDTO workOrderVehiculeDTO) {
        log.debug("Request to save WorkOrderVehicule : {}", workOrderVehiculeDTO);
        WorkOrderVehicule workOrderVehicule = workOrderVehiculeMapper.toEntity(workOrderVehiculeDTO);
        workOrderVehicule = workOrderVehiculeRepository.save(workOrderVehicule);
        return workOrderVehiculeMapper.toDto(workOrderVehicule);
    }

    /**
     * Partially update a workOrderVehicule.
     *
     * @param workOrderVehiculeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkOrderVehiculeDTO> partialUpdate(WorkOrderVehiculeDTO workOrderVehiculeDTO) {
        log.debug("Request to partially update WorkOrderVehicule : {}", workOrderVehiculeDTO);

        return workOrderVehiculeRepository
            .findById(workOrderVehiculeDTO.getId())
            .map(existingWorkOrderVehicule -> {
                workOrderVehiculeMapper.partialUpdate(existingWorkOrderVehicule, workOrderVehiculeDTO);

                return existingWorkOrderVehicule;
            })
            .map(workOrderVehiculeRepository::save)
            .map(workOrderVehiculeMapper::toDto);
    }

    /**
     * Get all the workOrderVehicules.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkOrderVehiculeDTO> findAll() {
        log.debug("Request to get all WorkOrderVehicules");
        return workOrderVehiculeRepository
            .findAll()
            .stream()
            .map(workOrderVehiculeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one workOrderVehicule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkOrderVehiculeDTO> findOne(Long id) {
        log.debug("Request to get WorkOrderVehicule : {}", id);
        return workOrderVehiculeRepository.findById(id).map(workOrderVehiculeMapper::toDto);
    }

    /**
     * Get all the workOrderVehicules linked to a given work order.
     *
     * @param workOrderId the id of the work order.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkOrderVehiculeDTO> findByWorkOrderId(Long workOrderId) {
        log.debug("Request to get WorkOrderVehicules by workOrder : {}", workOrderId);
        return workOrderVehiculeRepository
            .findByWorkOrderId(workOrderId)
            .stream()
            .map(workOrderVehiculeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Vérifie si les véhicules donnés sont déjà affectés à un autre WorkOrder
     * dont la mission n'est pas encore terminée (dateHeureFinPrev dans le futur).
     *
     * @param vehiculeIds les ids des véhicules à vérifier.
     * @param excludeWorkOrderId le work order courant à exclure (mode édition), peut être null.
     * @return la liste des conflits détectés (vide si tous disponibles).
     */
    @Transactional(readOnly = true)
    public List<VehiculeConflictDTO> findConflicts(List<Long> vehiculeIds, Long excludeWorkOrderId) {
        log.debug("Request to check vehicule disponibilité : {}, exclude={}", vehiculeIds, excludeWorkOrderId);

        if (vehiculeIds == null || vehiculeIds.isEmpty()) {
            return List.of();
        }

        List<WorkOrderVehicule> links = workOrderVehiculeRepository.findByVehiculeIdIn(vehiculeIds);

        if (links.isEmpty()) {
            return List.of();
        }

        List<Long> workOrderIds = links.stream().map(WorkOrderVehicule::getWorkOrderId).distinct().collect(Collectors.toList());

        Map<Long, WorkOrder> workOrdersById = workOrderRepository
            .findAllById(workOrderIds)
            .stream()
            .collect(Collectors.toMap(WorkOrder::getId, wo -> wo));

        ZonedDateTime now = ZonedDateTime.now();
        List<VehiculeConflictDTO> conflicts = new LinkedList<>();

        for (WorkOrderVehicule link : links) {
            WorkOrder wo = workOrdersById.get(link.getWorkOrderId());

            if (wo == null || wo.getDateHeureFinPrev() == null) {
                continue;
            }
            if (excludeWorkOrderId != null && excludeWorkOrderId.equals(wo.getId())) {
                continue;
            }
            if (wo.getDateHeureFinPrev().isAfter(now)) {
                conflicts.add(
                    new VehiculeConflictDTO(
                        link.getVehiculeId(),
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
     * Replace the full list of vehicules linked to a work order
     * (deletes the existing links, then re-creates them from the given vehicule ids).
     *
     * @param workOrderId the id of the work order.
     * @param vehiculeIds the ids of the selected vehicules.
     * @return the persisted entities.
     */
    public List<WorkOrderVehiculeDTO> replaceForWorkOrder(Long workOrderId, List<Long> vehiculeIds) {
        log.debug("Request to replace WorkOrderVehicules for workOrder : {}, {}", workOrderId, vehiculeIds);

        workOrderVehiculeRepository.deleteByWorkOrderId(workOrderId);

        List<WorkOrderVehicule> entities = vehiculeIds
            .stream()
            .map(vehiculeId -> new WorkOrderVehicule().workOrderId(workOrderId).vehiculeId(vehiculeId))
            .collect(Collectors.toCollection(LinkedList::new));

        return workOrderVehiculeRepository
            .saveAll(entities)
            .stream()
            .map(workOrderVehiculeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Delete the workOrderVehicule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrderVehicule : {}", id);
        workOrderVehiculeRepository.deleteById(id);
    }
}
