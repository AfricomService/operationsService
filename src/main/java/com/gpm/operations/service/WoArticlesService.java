package com.gpm.operations.service;

import com.gpm.operations.domain.WoArticles;
import com.gpm.operations.repository.WoArticlesRepository;
import com.gpm.operations.security.AuthoritiesConstants;
import com.gpm.operations.security.SecurityUtils;
import com.gpm.operations.service.dto.WoArticlesDTO;
import com.gpm.operations.service.mapper.WoArticlesMapper;
import com.gpm.operations.web.rest.errors.BadRequestAlertException;
import java.util.*;
import java.util.function.Function;
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

    @Transactional(readOnly = true)
    public List<WoArticlesDTO> findByWoId(Long woId) {
        List<WoArticlesDTO> result = woArticlesRepository
            .findAllByWoId(woId)
            .stream()
            .map(woArticlesMapper::toDto)
            .collect(Collectors.toList());

        maskPriceIfNotAllowed(result);
        return result;
    }

    private void maskPriceIfNotAllowed(List<WoArticlesDTO> dtos) {
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.CAN_SEE_PRICE)) {
            dtos.forEach(dto -> {
                dto.setPrixArticle(null);
                dto.setPrixArticleHT(null); // see question 5 below
            });
        }
    }

    public List<WoArticlesDTO> replaceForWo(Long woId, List<WoArticlesDTO> dtos) {
        Map<Long, WoArticles> existingByArticle = woArticlesRepository
            .findAllByWoId(woId)
            .stream()
            .collect(Collectors.toMap(WoArticles::getArticleId, Function.identity(), (a, b) -> a));

        Set<Long> incomingArticleIds = new HashSet<>();
        List<WoArticles> toSave = new ArrayList<>();

        for (WoArticlesDTO dto : dtos) {
            if (dto.getArticleId() == null || !incomingArticleIds.add(dto.getArticleId())) {
                throw new BadRequestAlertException("Article manquant ou en double", "operationsServiceWoArticles", "invalidarticle");
            }
            if (dto.getQteCommande() == null || dto.getQteCommande() < 1) {
                throw new BadRequestAlertException("Quantité invalide", "operationsServiceWoArticles", "invalidqte");
            }

            WoArticles entity = existingByArticle.get(dto.getArticleId());
            if (entity != null) {
                // Déjà lié : on ne change que la quantité commandée (prix figés, qteEffectuee, dateRealisation conservés)
                entity.setQteCommande(dto.getQteCommande());
            } else {
                entity = woArticlesMapper.toEntity(dto);
                entity.setId(null);
                entity.setWoId(woId);
                entity.setQteEffectuee(0);
                entity.setDateRealisation(null);
            }
            toSave.add(entity);
        }

        existingByArticle
            .values()
            .stream()
            .filter(e -> !incomingArticleIds.contains(e.getArticleId()))
            .forEach(woArticlesRepository::delete);

        return woArticlesRepository.saveAll(toSave).stream().map(woArticlesMapper::toDto).collect(Collectors.toList());
    }
}
