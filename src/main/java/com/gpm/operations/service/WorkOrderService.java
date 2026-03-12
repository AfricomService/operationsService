package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.service.dto.WorkOrderDTO;
import com.gpm.operations.service.mapper.WorkOrderMapper;
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
