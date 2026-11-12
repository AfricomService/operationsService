package com.gpm.operations.service;

import com.gpm.operations.domain.WoArticles;
import com.gpm.operations.repository.WoArticlesRepository;
import com.gpm.operations.service.dto.WoArticlesDTO;
import com.gpm.operations.service.mapper.WoArticlesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WoArticles}.
 */
@Service
@Transactional
public class WoArticlesService {

    private final Logger log = LoggerFactory.getLogger(WoArticlesService.class);

    private final WoArticlesRepository woArticlesRepository;

    private final WoArticlesMapper woArticlesMapper;

    public WoArticlesService(WoArticlesRepository woArticlesRepository, WoArticlesMapper woArticlesMapper) {
        this.woArticlesRepository = woArticlesRepository;
        this.woArticlesMapper = woArticlesMapper;
    }

    /**
     * Save a woArticles.
     *
     * @param woArticlesDTO the entity to save.
     * @return the persisted entity.
     */
    public WoArticlesDTO save(WoArticlesDTO woArticlesDTO) {
        log.debug("Request to save WoArticles : {}", woArticlesDTO);
        WoArticles woArticles = woArticlesMapper.toEntity(woArticlesDTO);
        woArticles = woArticlesRepository.save(woArticles);
        return woArticlesMapper.toDto(woArticles);
    }

    /**
     * Update a woArticles.
     *
     * @param woArticlesDTO the entity to save.
     * @return the persisted entity.
     */
    public WoArticlesDTO update(WoArticlesDTO woArticlesDTO) {
        log.debug("Request to update WoArticles : {}", woArticlesDTO);
        WoArticles woArticles = woArticlesMapper.toEntity(woArticlesDTO);
        woArticles = woArticlesRepository.save(woArticles);
        return woArticlesMapper.toDto(woArticles);
    }

    /**
     * Partially update a woArticles.
     *
     * @param woArticlesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WoArticlesDTO> partialUpdate(WoArticlesDTO woArticlesDTO) {
        log.debug("Request to partially update WoArticles : {}", woArticlesDTO);

        return woArticlesRepository
            .findById(woArticlesDTO.getId())
            .map(existingWoArticles -> {
                woArticlesMapper.partialUpdate(existingWoArticles, woArticlesDTO);

                return existingWoArticles;
            })
            .map(woArticlesRepository::save)
            .map(woArticlesMapper::toDto);
    }

    /**
     * Get all the woArticles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WoArticlesDTO> findAll() {
        log.debug("Request to get all WoArticles");
        return woArticlesRepository.findAll().stream().map(woArticlesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one woArticles by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WoArticlesDTO> findOne(Long id) {
        log.debug("Request to get WoArticles : {}", id);
        return woArticlesRepository.findById(id).map(woArticlesMapper::toDto);
    }

    /**
     * Delete the woArticles by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WoArticles : {}", id);
        woArticlesRepository.deleteById(id);
    }
}
