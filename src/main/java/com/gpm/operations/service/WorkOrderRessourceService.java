package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrderRessource;
import com.gpm.operations.repository.WorkOrderRessourceRepository;
import com.gpm.operations.service.dto.WorkOrderRessourceDTO;
import com.gpm.operations.service.mapper.WorkOrderRessourceMapper;
import java.util.LinkedList;
import java.util.List;
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

    public WorkOrderRessourceService(
        WorkOrderRessourceRepository workOrderRessourceRepository,
        WorkOrderRessourceMapper workOrderRessourceMapper
    ) {
        this.workOrderRessourceRepository = workOrderRessourceRepository;
        this.workOrderRessourceMapper = workOrderRessourceMapper;
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
     * Delete the workOrderRessource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrderRessource : {}", id);
        workOrderRessourceRepository.deleteById(id);
    }
}
