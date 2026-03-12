package com.gpm.operations.service;

import com.gpm.operations.domain.Activite;
import com.gpm.operations.repository.ActiviteRepository;
import com.gpm.operations.service.dto.ActiviteDTO;
import com.gpm.operations.service.mapper.ActiviteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Activite}.
 */
@Service
@Transactional
public class ActiviteService {

    private final Logger log = LoggerFactory.getLogger(ActiviteService.class);

    private final ActiviteRepository activiteRepository;

    private final ActiviteMapper activiteMapper;

    public ActiviteService(ActiviteRepository activiteRepository, ActiviteMapper activiteMapper) {
        this.activiteRepository = activiteRepository;
        this.activiteMapper = activiteMapper;
    }

    /**
     * Save a activite.
     *
     * @param activiteDTO the entity to save.
     * @return the persisted entity.
     */
    public ActiviteDTO save(ActiviteDTO activiteDTO) {
        log.debug("Request to save Activite : {}", activiteDTO);
        Activite activite = activiteMapper.toEntity(activiteDTO);
        activite = activiteRepository.save(activite);
        return activiteMapper.toDto(activite);
    }

    /**
     * Update a activite.
     *
     * @param activiteDTO the entity to save.
     * @return the persisted entity.
     */
    public ActiviteDTO update(ActiviteDTO activiteDTO) {
        log.debug("Request to update Activite : {}", activiteDTO);
        Activite activite = activiteMapper.toEntity(activiteDTO);
        activite = activiteRepository.save(activite);
        return activiteMapper.toDto(activite);
    }

    /**
     * Partially update a activite.
     *
     * @param activiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ActiviteDTO> partialUpdate(ActiviteDTO activiteDTO) {
        log.debug("Request to partially update Activite : {}", activiteDTO);

        return activiteRepository
            .findById(activiteDTO.getId())
            .map(existingActivite -> {
                activiteMapper.partialUpdate(existingActivite, activiteDTO);

                return existingActivite;
            })
            .map(activiteRepository::save)
            .map(activiteMapper::toDto);
    }

    /**
     * Get all the activites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ActiviteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Activites");
        return activiteRepository.findAll(pageable).map(activiteMapper::toDto);
    }

    /**
     * Get one activite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActiviteDTO> findOne(Long id) {
        log.debug("Request to get Activite : {}", id);
        return activiteRepository.findById(id).map(activiteMapper::toDto);
    }

    /**
     * Delete the activite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Activite : {}", id);
        activiteRepository.deleteById(id);
    }
}
