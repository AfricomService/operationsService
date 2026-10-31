package com.gpm.operations.service;

import com.gpm.operations.domain.ParametrageDeplacement;
import com.gpm.operations.repository.ParametrageDeplacementRepository;
import com.gpm.operations.service.dto.ParametrageDeplacementDTO;
import com.gpm.operations.service.mapper.ParametrageDeplacementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParametrageDeplacement}.
 */
@Service
@Transactional
public class ParametrageDeplacementService {

    private final Logger log = LoggerFactory.getLogger(ParametrageDeplacementService.class);

    private final ParametrageDeplacementRepository parametrageDeplacementRepository;

    private final ParametrageDeplacementMapper parametrageDeplacementMapper;

    public ParametrageDeplacementService(
        ParametrageDeplacementRepository parametrageDeplacementRepository,
        ParametrageDeplacementMapper parametrageDeplacementMapper
    ) {
        this.parametrageDeplacementRepository = parametrageDeplacementRepository;
        this.parametrageDeplacementMapper = parametrageDeplacementMapper;
    }

    /**
     * Save a parametrageDeplacement.
     *
     * @param parametrageDeplacementDTO the entity to save.
     * @return the persisted entity.
     */
    public ParametrageDeplacementDTO save(ParametrageDeplacementDTO parametrageDeplacementDTO) {
        log.debug("Request to save ParametrageDeplacement : {}", parametrageDeplacementDTO);
        ParametrageDeplacement parametrageDeplacement = parametrageDeplacementMapper.toEntity(parametrageDeplacementDTO);
        parametrageDeplacement = parametrageDeplacementRepository.save(parametrageDeplacement);
        return parametrageDeplacementMapper.toDto(parametrageDeplacement);
    }

    /**
     * Partially update a parametrageDeplacement.
     *
     * @param parametrageDeplacementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParametrageDeplacementDTO> partialUpdate(ParametrageDeplacementDTO parametrageDeplacementDTO) {
        log.debug("Request to partially update ParametrageDeplacement : {}", parametrageDeplacementDTO);

        return parametrageDeplacementRepository
            .findById(parametrageDeplacementDTO.getId())
            .map(existingParametrageDeplacement -> {
                parametrageDeplacementMapper.partialUpdate(existingParametrageDeplacement, parametrageDeplacementDTO);

                return existingParametrageDeplacement;
            })
            .map(parametrageDeplacementRepository::save)
            .map(parametrageDeplacementMapper::toDto);
    }

    /**
     * Get all the parametrageDeplacements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParametrageDeplacementDTO> findAll() {
        log.debug("Request to get all ParametrageDeplacements");
        return parametrageDeplacementRepository
            .findAll()
            .stream()
            .map(parametrageDeplacementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one parametrageDeplacement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParametrageDeplacementDTO> findOne(Long id) {
        log.debug("Request to get ParametrageDeplacement : {}", id);
        return parametrageDeplacementRepository.findById(id).map(parametrageDeplacementMapper::toDto);
    }

    /**
     * Get all the parametrageDeplacements of a work order (one row per day).
     */
    @Transactional(readOnly = true)
    public List<ParametrageDeplacementDTO> findByWorkOrder(Long workOrderId) {
        log.debug("Request to get ParametrageDeplacements of WorkOrder : {}", workOrderId);
        return parametrageDeplacementRepository
            .findByWorkOrderIdOrderByDateAsc(workOrderId)
            .stream()
            .map(parametrageDeplacementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Replace all the parametrageDeplacements of a work order by the given list (one row per day).
     */
    public List<ParametrageDeplacementDTO> replaceForWorkOrder(Long workOrderId, List<ParametrageDeplacementDTO> dtos) {
        log.debug("Request to replace ParametrageDeplacements of WorkOrder : {}", workOrderId);
        parametrageDeplacementRepository.deleteByWorkOrderId(workOrderId);
        parametrageDeplacementRepository.flush();

        List<ParametrageDeplacement> entities = dtos
            .stream()
            .map(dto -> {
                dto.setId(null);
                dto.setWorkOrderId(workOrderId);
                return parametrageDeplacementMapper.toEntity(dto);
            })
            .collect(Collectors.toList());

        return parametrageDeplacementRepository
            .saveAll(entities)
            .stream()
            .map(parametrageDeplacementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Delete the parametrageDeplacement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParametrageDeplacement : {}", id);
        parametrageDeplacementRepository.deleteById(id);
    }
}
