package com.gpm.operations.service;

import com.gpm.operations.domain.Motif;
import com.gpm.operations.repository.MotifRepository;
import com.gpm.operations.service.dto.MotifDTO;
import com.gpm.operations.service.mapper.MotifMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Motif}.
 */
@Service
@Transactional
public class MotifService {

    private final Logger log = LoggerFactory.getLogger(MotifService.class);

    private final MotifRepository motifRepository;

    private final MotifMapper motifMapper;

    public MotifService(MotifRepository motifRepository, MotifMapper motifMapper) {
        this.motifRepository = motifRepository;
        this.motifMapper = motifMapper;
    }

    /**
     * Save a motif.
     *
     * @param motifDTO the entity to save.
     * @return the persisted entity.
     */
    public MotifDTO save(MotifDTO motifDTO) {
        log.debug("Request to save Motif : {}", motifDTO);
        Motif motif = motifMapper.toEntity(motifDTO);
        motif = motifRepository.save(motif);
        return motifMapper.toDto(motif);
    }

    /**
     * Update a motif.
     *
     * @param motifDTO the entity to save.
     * @return the persisted entity.
     */
    public MotifDTO update(MotifDTO motifDTO) {
        log.debug("Request to update Motif : {}", motifDTO);
        Motif motif = motifMapper.toEntity(motifDTO);
        motif = motifRepository.save(motif);
        return motifMapper.toDto(motif);
    }

    /**
     * Partially update a motif.
     *
     * @param motifDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MotifDTO> partialUpdate(MotifDTO motifDTO) {
        log.debug("Request to partially update Motif : {}", motifDTO);

        return motifRepository
            .findById(motifDTO.getId())
            .map(existingMotif -> {
                motifMapper.partialUpdate(existingMotif, motifDTO);

                return existingMotif;
            })
            .map(motifRepository::save)
            .map(motifMapper::toDto);
    }

    /**
     * Get all the motifs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MotifDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Motifs");
        return motifRepository.findAll(pageable).map(motifMapper::toDto);
    }

    /**
     * Get one motif by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MotifDTO> findOne(Long id) {
        log.debug("Request to get Motif : {}", id);
        return motifRepository.findById(id).map(motifMapper::toDto);
    }

    /**
     * Delete the motif by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Motif : {}", id);
        motifRepository.deleteById(id);
    }
}
