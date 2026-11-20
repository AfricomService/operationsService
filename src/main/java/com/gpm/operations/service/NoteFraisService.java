package com.gpm.operations.service;

import com.gpm.operations.domain.NoteFrais;
import com.gpm.operations.repository.NoteFraisRepository;
import com.gpm.operations.service.dto.NoteFraisDTO;
import com.gpm.operations.service.mapper.NoteFraisMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NoteFrais}.
 */
@Service
@Transactional
public class NoteFraisService {

    private final Logger log = LoggerFactory.getLogger(NoteFraisService.class);

    private final NoteFraisRepository noteFraisRepository;

    private final NoteFraisMapper noteFraisMapper;

    public NoteFraisService(NoteFraisRepository noteFraisRepository, NoteFraisMapper noteFraisMapper) {
        this.noteFraisRepository = noteFraisRepository;
        this.noteFraisMapper = noteFraisMapper;
    }

    /**
     * Save a noteFrais.
     *
     * @param noteFraisDTO the entity to save.
     * @return the persisted entity.
     */
    public NoteFraisDTO save(NoteFraisDTO noteFraisDTO) {
        log.debug("Request to save NoteFrais : {}", noteFraisDTO);
        NoteFrais noteFrais = noteFraisMapper.toEntity(noteFraisDTO);
        noteFrais = noteFraisRepository.save(noteFrais);
        return noteFraisMapper.toDto(noteFrais);
    }

    /**
     * Partially update a noteFrais.
     *
     * @param noteFraisDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NoteFraisDTO> partialUpdate(NoteFraisDTO noteFraisDTO) {
        log.debug("Request to partially update NoteFrais : {}", noteFraisDTO);

        return noteFraisRepository
            .findById(noteFraisDTO.getId())
            .map(existingNoteFrais -> {
                noteFraisMapper.partialUpdate(existingNoteFrais, noteFraisDTO);

                return existingNoteFrais;
            })
            .map(noteFraisRepository::save)
            .map(noteFraisMapper::toDto);
    }

    /**
     * Get all the noteFrais.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NoteFraisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NoteFrais");
        return noteFraisRepository.findAll(pageable).map(noteFraisMapper::toDto);
    }

    /**
     * Get one noteFrais by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NoteFraisDTO> findOne(Long id) {
        log.debug("Request to get NoteFrais : {}", id);
        return noteFraisRepository.findById(id).map(noteFraisMapper::toDto);
    }

    /**
     * Delete the noteFrais by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NoteFrais : {}", id);
        noteFraisRepository.deleteById(id);
    }
}
