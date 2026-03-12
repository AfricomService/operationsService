package com.gpm.operations.service;

import com.gpm.operations.domain.WoSite;
import com.gpm.operations.repository.WoSiteRepository;
import com.gpm.operations.service.dto.WoSiteDTO;
import com.gpm.operations.service.mapper.WoSiteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WoSite}.
 */
@Service
@Transactional
public class WoSiteService {

    private final Logger log = LoggerFactory.getLogger(WoSiteService.class);

    private final WoSiteRepository woSiteRepository;

    private final WoSiteMapper woSiteMapper;

    public WoSiteService(WoSiteRepository woSiteRepository, WoSiteMapper woSiteMapper) {
        this.woSiteRepository = woSiteRepository;
        this.woSiteMapper = woSiteMapper;
    }

    /**
     * Save a woSite.
     *
     * @param woSiteDTO the entity to save.
     * @return the persisted entity.
     */
    public WoSiteDTO save(WoSiteDTO woSiteDTO) {
        log.debug("Request to save WoSite : {}", woSiteDTO);
        WoSite woSite = woSiteMapper.toEntity(woSiteDTO);
        woSite = woSiteRepository.save(woSite);
        return woSiteMapper.toDto(woSite);
    }

    /**
     * Update a woSite.
     *
     * @param woSiteDTO the entity to save.
     * @return the persisted entity.
     */
    public WoSiteDTO update(WoSiteDTO woSiteDTO) {
        log.debug("Request to update WoSite : {}", woSiteDTO);
        WoSite woSite = woSiteMapper.toEntity(woSiteDTO);
        woSite = woSiteRepository.save(woSite);
        return woSiteMapper.toDto(woSite);
    }

    /**
     * Partially update a woSite.
     *
     * @param woSiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WoSiteDTO> partialUpdate(WoSiteDTO woSiteDTO) {
        log.debug("Request to partially update WoSite : {}", woSiteDTO);

        return woSiteRepository
            .findById(woSiteDTO.getId())
            .map(existingWoSite -> {
                woSiteMapper.partialUpdate(existingWoSite, woSiteDTO);

                return existingWoSite;
            })
            .map(woSiteRepository::save)
            .map(woSiteMapper::toDto);
    }

    /**
     * Get all the woSites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WoSiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WoSites");
        return woSiteRepository.findAll(pageable).map(woSiteMapper::toDto);
    }

    /**
     * Get one woSite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WoSiteDTO> findOne(Long id) {
        log.debug("Request to get WoSite : {}", id);
        return woSiteRepository.findById(id).map(woSiteMapper::toDto);
    }

    /**
     * Delete the woSite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WoSite : {}", id);
        woSiteRepository.deleteById(id);
    }
}
