package com.gpm.operations.service;

import com.gpm.operations.domain.Tache;
import com.gpm.operations.repository.TacheRepository;
import com.gpm.operations.service.dto.TacheDTO;
import com.gpm.operations.service.mapper.TacheMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tache}.
 */
@Service
@Transactional
public class TacheService {

    private final Logger log = LoggerFactory.getLogger(TacheService.class);

    private final TacheRepository tacheRepository;

    private final TacheMapper tacheMapper;

    public TacheService(TacheRepository tacheRepository, TacheMapper tacheMapper) {
        this.tacheRepository = tacheRepository;
        this.tacheMapper = tacheMapper;
    }

    /**
     * Save a tache.
     *
     * @param tacheDTO the entity to save.
     * @return the persisted entity.
     */
    public TacheDTO save(TacheDTO tacheDTO) {
        log.debug("Request to save Tache : {}", tacheDTO);
        Tache tache = tacheMapper.toEntity(tacheDTO);
        tache = tacheRepository.save(tache);
        return tacheMapper.toDto(tache);
    }

    /**
     * Update a tache.
     *
     * @param tacheDTO the entity to save.
     * @return the persisted entity.
     */
    public TacheDTO update(TacheDTO tacheDTO) {
        log.debug("Request to update Tache : {}", tacheDTO);
        Tache tache = tacheMapper.toEntity(tacheDTO);
        tache = tacheRepository.save(tache);
        return tacheMapper.toDto(tache);
    }

    /**
     * Partially update a tache.
     *
     * @param tacheDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TacheDTO> partialUpdate(TacheDTO tacheDTO) {
        log.debug("Request to partially update Tache : {}", tacheDTO);

        return tacheRepository
            .findById(tacheDTO.getId())
            .map(existingTache -> {
                tacheMapper.partialUpdate(existingTache, tacheDTO);

                return existingTache;
            })
            .map(tacheRepository::save)
            .map(tacheMapper::toDto);
    }

    /**
     * Get all the taches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TacheDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Taches");
        return tacheRepository.findAll(pageable).map(tacheMapper::toDto);
    }

    /**
     * Get all the taches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TacheDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tacheRepository.findAllWithEagerRelationships(pageable).map(tacheMapper::toDto);
    }

    /**
     * Get one tache by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TacheDTO> findOne(Long id) {
        log.debug("Request to get Tache : {}", id);
        return tacheRepository.findOneWithEagerRelationships(id).map(tacheMapper::toDto);
    }

    /**
     * Delete the tache by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tache : {}", id);
        tacheRepository.deleteById(id);
    }
}
