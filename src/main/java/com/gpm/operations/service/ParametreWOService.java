package com.gpm.operations.service;

import com.gpm.operations.domain.ParametreWO;
import com.gpm.operations.repository.ParametreWORepository;
import com.gpm.operations.service.dto.ParametreWODTO;
import com.gpm.operations.service.mapper.ParametreWOMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParametreWO}.
 */
@Service
@Transactional
public class ParametreWOService {

    private final Logger log = LoggerFactory.getLogger(ParametreWOService.class);

    private final ParametreWORepository parametreWORepository;

    private final ParametreWOMapper parametreWOMapper;

    public ParametreWOService(ParametreWORepository parametreWORepository, ParametreWOMapper parametreWOMapper) {
        this.parametreWORepository = parametreWORepository;
        this.parametreWOMapper = parametreWOMapper;
    }

    /**
     * Save a parametreWO.
     *
     * @param parametreWODTO the entity to save.
     * @return the persisted entity.
     */
    public ParametreWODTO save(ParametreWODTO parametreWODTO) {
        log.debug("Request to save ParametreWO : {}", parametreWODTO);
        ParametreWO parametreWO = parametreWOMapper.toEntity(parametreWODTO);
        parametreWO = parametreWORepository.save(parametreWO);
        return parametreWOMapper.toDto(parametreWO);
    }

    /**
     * Partially update a parametreWO.
     *
     * @param parametreWODTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParametreWODTO> partialUpdate(ParametreWODTO parametreWODTO) {
        log.debug("Request to partially update ParametreWO : {}", parametreWODTO);

        return parametreWORepository
            .findById(parametreWODTO.getId())
            .map(existingParametreWO -> {
                parametreWOMapper.partialUpdate(existingParametreWO, parametreWODTO);

                return existingParametreWO;
            })
            .map(parametreWORepository::save)
            .map(parametreWOMapper::toDto);
    }

    /**
     * Get all the parametreWOS.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParametreWODTO> findAll() {
        log.debug("Request to get all ParametreWOS");
        return parametreWORepository.findAll().stream().map(parametreWOMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one parametreWO by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParametreWODTO> findOne(Long id) {
        log.debug("Request to get ParametreWO : {}", id);
        return parametreWORepository.findById(id).map(parametreWOMapper::toDto);
    }

    /**
     * Delete the parametreWO by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParametreWO : {}", id);
        parametreWORepository.deleteById(id);
    }
}
