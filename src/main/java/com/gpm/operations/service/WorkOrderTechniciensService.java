package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrderTechniciens;
import com.gpm.operations.repository.WorkOrderTechniciensRepository;
import com.gpm.operations.service.dto.WorkOrderTechniciensDTO;
import com.gpm.operations.service.mapper.WorkOrderTechniciensMapper;
import java.util.LinkedList;
import java.util.List;
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

    public WorkOrderTechniciensService(
        WorkOrderTechniciensRepository workOrderTechniciensRepository,
        WorkOrderTechniciensMapper workOrderTechniciensMapper
    ) {
        this.workOrderTechniciensRepository = workOrderTechniciensRepository;
        this.workOrderTechniciensMapper = workOrderTechniciensMapper;
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
