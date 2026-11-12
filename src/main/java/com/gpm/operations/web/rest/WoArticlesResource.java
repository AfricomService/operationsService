package com.gpm.operations.web.rest;

import com.gpm.operations.repository.WoArticlesRepository;
import com.gpm.operations.service.WoArticlesService;
import com.gpm.operations.service.dto.WoArticlesDTO;
import com.gpm.operations.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gpm.operations.domain.WoArticles}.
 */
@RestController
@RequestMapping("/api")
public class WoArticlesResource {

    private final Logger log = LoggerFactory.getLogger(WoArticlesResource.class);

    private static final String ENTITY_NAME = "operationsServiceWoArticles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WoArticlesService woArticlesService;

    private final WoArticlesRepository woArticlesRepository;

    public WoArticlesResource(WoArticlesService woArticlesService, WoArticlesRepository woArticlesRepository) {
        this.woArticlesService = woArticlesService;
        this.woArticlesRepository = woArticlesRepository;
    }

    /**
     * {@code POST  /wo-articles} : Create a new woArticles.
     *
     * @param woArticlesDTO the woArticlesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new woArticlesDTO, or with status {@code 400 (Bad Request)} if the woArticles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wo-articles")
    public ResponseEntity<WoArticlesDTO> createWoArticles(@RequestBody WoArticlesDTO woArticlesDTO) throws URISyntaxException {
        log.debug("REST request to save WoArticles : {}", woArticlesDTO);
        if (woArticlesDTO.getId() != null) {
            throw new BadRequestAlertException("A new woArticles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WoArticlesDTO result = woArticlesService.save(woArticlesDTO);
        return ResponseEntity
            .created(new URI("/api/wo-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wo-articles/:id} : Updates an existing woArticles.
     *
     * @param id the id of the woArticlesDTO to save.
     * @param woArticlesDTO the woArticlesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated woArticlesDTO,
     * or with status {@code 400 (Bad Request)} if the woArticlesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the woArticlesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wo-articles/{id}")
    public ResponseEntity<WoArticlesDTO> updateWoArticles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WoArticlesDTO woArticlesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WoArticles : {}, {}", id, woArticlesDTO);
        if (woArticlesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, woArticlesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!woArticlesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WoArticlesDTO result = woArticlesService.update(woArticlesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woArticlesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wo-articles/:id} : Partial updates given fields of an existing woArticles, field will ignore if it is null
     *
     * @param id the id of the woArticlesDTO to save.
     * @param woArticlesDTO the woArticlesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated woArticlesDTO,
     * or with status {@code 400 (Bad Request)} if the woArticlesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the woArticlesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the woArticlesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wo-articles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WoArticlesDTO> partialUpdateWoArticles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WoArticlesDTO woArticlesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WoArticles partially : {}, {}", id, woArticlesDTO);
        if (woArticlesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, woArticlesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!woArticlesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WoArticlesDTO> result = woArticlesService.partialUpdate(woArticlesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woArticlesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wo-articles} : get all the woArticles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of woArticles in body.
     */
    @GetMapping("/wo-articles")
    public List<WoArticlesDTO> getAllWoArticles() {
        log.debug("REST request to get all WoArticles");
        return woArticlesService.findAll();
    }

    /**
     * {@code GET  /wo-articles/:id} : get the "id" woArticles.
     *
     * @param id the id of the woArticlesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the woArticlesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wo-articles/{id}")
    public ResponseEntity<WoArticlesDTO> getWoArticles(@PathVariable Long id) {
        log.debug("REST request to get WoArticles : {}", id);
        Optional<WoArticlesDTO> woArticlesDTO = woArticlesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(woArticlesDTO);
    }

    /**
     * {@code DELETE  /wo-articles/:id} : delete the "id" woArticles.
     *
     * @param id the id of the woArticlesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wo-articles/{id}")
    public ResponseEntity<Void> deleteWoArticles(@PathVariable Long id) {
        log.debug("REST request to delete WoArticles : {}", id);
        woArticlesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/wo-articles/by-wo/{woId}")
    public List<WoArticlesDTO> getWoArticlesByWo(@PathVariable Long woId) {
        return woArticlesService.findByWoId(woId);
    }

    @PutMapping("/wo-articles/wo/{woId}")
    public ResponseEntity<List<WoArticlesDTO>> replaceWoArticles(@PathVariable Long woId, @RequestBody List<WoArticlesDTO> dtos) {
        List<WoArticlesDTO> result = woArticlesService.replaceForWo(woId, dtos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woId.toString()))
            .body(result);
    }
}
