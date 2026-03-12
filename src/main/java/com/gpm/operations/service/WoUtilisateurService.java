package com.gpm.operations.service;

import com.gpm.operations.domain.WoUtilisateur;
import com.gpm.operations.repository.WoUtilisateurRepository;
import com.gpm.operations.service.dto.WoUtilisateurDTO;
import com.gpm.operations.service.mapper.WoUtilisateurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WoUtilisateur}.
 */
@Service
@Transactional
public class WoUtilisateurService {

    private final Logger log = LoggerFactory.getLogger(WoUtilisateurService.class);

    private final WoUtilisateurRepository woUtilisateurRepository;

    private final WoUtilisateurMapper woUtilisateurMapper;

    public WoUtilisateurService(WoUtilisateurRepository woUtilisateurRepository, WoUtilisateurMapper woUtilisateurMapper) {
        this.woUtilisateurRepository = woUtilisateurRepository;
        this.woUtilisateurMapper = woUtilisateurMapper;
    }

    /**
     * Save a woUtilisateur.
     *
     * @param woUtilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public WoUtilisateurDTO save(WoUtilisateurDTO woUtilisateurDTO) {
        log.debug("Request to save WoUtilisateur : {}", woUtilisateurDTO);
        WoUtilisateur woUtilisateur = woUtilisateurMapper.toEntity(woUtilisateurDTO);
        woUtilisateur = woUtilisateurRepository.save(woUtilisateur);
        return woUtilisateurMapper.toDto(woUtilisateur);
    }

    /**
     * Update a woUtilisateur.
     *
     * @param woUtilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public WoUtilisateurDTO update(WoUtilisateurDTO woUtilisateurDTO) {
        log.debug("Request to update WoUtilisateur : {}", woUtilisateurDTO);
        WoUtilisateur woUtilisateur = woUtilisateurMapper.toEntity(woUtilisateurDTO);
        woUtilisateur = woUtilisateurRepository.save(woUtilisateur);
        return woUtilisateurMapper.toDto(woUtilisateur);
    }

    /**
     * Partially update a woUtilisateur.
     *
     * @param woUtilisateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WoUtilisateurDTO> partialUpdate(WoUtilisateurDTO woUtilisateurDTO) {
        log.debug("Request to partially update WoUtilisateur : {}", woUtilisateurDTO);

        return woUtilisateurRepository
            .findById(woUtilisateurDTO.getId())
            .map(existingWoUtilisateur -> {
                woUtilisateurMapper.partialUpdate(existingWoUtilisateur, woUtilisateurDTO);

                return existingWoUtilisateur;
            })
            .map(woUtilisateurRepository::save)
            .map(woUtilisateurMapper::toDto);
    }

    /**
     * Get all the woUtilisateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WoUtilisateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WoUtilisateurs");
        return woUtilisateurRepository.findAll(pageable).map(woUtilisateurMapper::toDto);
    }

    /**
     * Get one woUtilisateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WoUtilisateurDTO> findOne(Long id) {
        log.debug("Request to get WoUtilisateur : {}", id);
        return woUtilisateurRepository.findById(id).map(woUtilisateurMapper::toDto);
    }

    /**
     * Delete the woUtilisateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WoUtilisateur : {}", id);
        woUtilisateurRepository.deleteById(id);
    }
}
