package com.gpm.operations.service;

import com.gpm.operations.domain.WoMotif;
import com.gpm.operations.repository.WoMotifRepository;
import com.gpm.operations.service.dto.WoMotifDTO;
import com.gpm.operations.service.mapper.WoMotifMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WoMotif}.
 */
@Service
@Transactional
public class WoMotifService {

    private final Logger log = LoggerFactory.getLogger(WoMotifService.class);

    private final WoMotifRepository woMotifRepository;

    private final WoMotifMapper woMotifMapper;

    public WoMotifService(WoMotifRepository woMotifRepository, WoMotifMapper woMotifMapper) {
        this.woMotifRepository = woMotifRepository;
        this.woMotifMapper = woMotifMapper;
    }

    /**
     * Save a woMotif.
     *
     * @param woMotifDTO the entity to save.
     * @return the persisted entity.
     */
    public WoMotifDTO save(WoMotifDTO woMotifDTO) {
        log.debug("Request to save WoMotif : {}", woMotifDTO);
        WoMotif woMotif = woMotifMapper.toEntity(woMotifDTO);
        woMotif = woMotifRepository.save(woMotif);
        return woMotifMapper.toDto(woMotif);
    }

    /**
     * Update a woMotif.
     *
     * @param woMotifDTO the entity to save.
     * @return the persisted entity.
     */
    public WoMotifDTO update(WoMotifDTO woMotifDTO) {
        log.debug("Request to update WoMotif : {}", woMotifDTO);
        WoMotif woMotif = woMotifMapper.toEntity(woMotifDTO);
        woMotif = woMotifRepository.save(woMotif);
        return woMotifMapper.toDto(woMotif);
    }

    /**
     * Partially update a woMotif.
     *
     * @param woMotifDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WoMotifDTO> partialUpdate(WoMotifDTO woMotifDTO) {
        log.debug("Request to partially update WoMotif : {}", woMotifDTO);

        return woMotifRepository
            .findById(woMotifDTO.getId())
            .map(existingWoMotif -> {
                woMotifMapper.partialUpdate(existingWoMotif, woMotifDTO);

                return existingWoMotif;
            })
            .map(woMotifRepository::save)
            .map(woMotifMapper::toDto);
    }

    /**
     * Get all the woMotifs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WoMotifDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WoMotifs");
        return woMotifRepository.findAll(pageable).map(woMotifMapper::toDto);
    }

    /**
     * Get all the woMotifs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WoMotifDTO> findAllWithEagerRelationships(Pageable pageable) {
        return woMotifRepository.findAllWithEagerRelationships(pageable).map(woMotifMapper::toDto);
    }

    /**
     * Get one woMotif by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WoMotifDTO> findOne(Long id) {
        log.debug("Request to get WoMotif : {}", id);
        return woMotifRepository.findOneWithEagerRelationships(id).map(woMotifMapper::toDto);
    }

    /**
     * Delete the woMotif by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WoMotif : {}", id);
        woMotifRepository.deleteById(id);
    }
}
