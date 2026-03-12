package com.gpm.operations.web.rest;

import com.gpm.operations.repository.WoSiteRepository;
import com.gpm.operations.service.WoSiteService;
import com.gpm.operations.service.dto.WoSiteDTO;
import com.gpm.operations.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gpm.operations.domain.WoSite}.
 */
@RestController
@RequestMapping("/api")
public class WoSiteResource {

    private final Logger log = LoggerFactory.getLogger(WoSiteResource.class);

    private static final String ENTITY_NAME = "operationsServiceWoSite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WoSiteService woSiteService;

    private final WoSiteRepository woSiteRepository;

    public WoSiteResource(WoSiteService woSiteService, WoSiteRepository woSiteRepository) {
        this.woSiteService = woSiteService;
        this.woSiteRepository = woSiteRepository;
    }

    /**
     * {@code POST  /wo-sites} : Create a new woSite.
     *
     * @param woSiteDTO the woSiteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new woSiteDTO, or with status {@code 400 (Bad Request)} if the woSite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wo-sites")
    public ResponseEntity<WoSiteDTO> createWoSite(@Valid @RequestBody WoSiteDTO woSiteDTO) throws URISyntaxException {
        log.debug("REST request to save WoSite : {}", woSiteDTO);
        if (woSiteDTO.getId() != null) {
            throw new BadRequestAlertException("A new woSite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WoSiteDTO result = woSiteService.save(woSiteDTO);
        return ResponseEntity
            .created(new URI("/api/wo-sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wo-sites/:id} : Updates an existing woSite.
     *
     * @param id the id of the woSiteDTO to save.
     * @param woSiteDTO the woSiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated woSiteDTO,
     * or with status {@code 400 (Bad Request)} if the woSiteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the woSiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wo-sites/{id}")
    public ResponseEntity<WoSiteDTO> updateWoSite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WoSiteDTO woSiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WoSite : {}, {}", id, woSiteDTO);
        if (woSiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, woSiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!woSiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WoSiteDTO result = woSiteService.update(woSiteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woSiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wo-sites/:id} : Partial updates given fields of an existing woSite, field will ignore if it is null
     *
     * @param id the id of the woSiteDTO to save.
     * @param woSiteDTO the woSiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated woSiteDTO,
     * or with status {@code 400 (Bad Request)} if the woSiteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the woSiteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the woSiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wo-sites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WoSiteDTO> partialUpdateWoSite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WoSiteDTO woSiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WoSite partially : {}, {}", id, woSiteDTO);
        if (woSiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, woSiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!woSiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WoSiteDTO> result = woSiteService.partialUpdate(woSiteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woSiteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wo-sites} : get all the woSites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of woSites in body.
     */
    @GetMapping("/wo-sites")
    public ResponseEntity<List<WoSiteDTO>> getAllWoSites(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WoSites");
        Page<WoSiteDTO> page = woSiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wo-sites/:id} : get the "id" woSite.
     *
     * @param id the id of the woSiteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the woSiteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wo-sites/{id}")
    public ResponseEntity<WoSiteDTO> getWoSite(@PathVariable Long id) {
        log.debug("REST request to get WoSite : {}", id);
        Optional<WoSiteDTO> woSiteDTO = woSiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(woSiteDTO);
    }

    /**
     * {@code DELETE  /wo-sites/:id} : delete the "id" woSite.
     *
     * @param id the id of the woSiteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wo-sites/{id}")
    public ResponseEntity<Void> deleteWoSite(@PathVariable Long id) {
        log.debug("REST request to delete WoSite : {}", id);
        woSiteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
