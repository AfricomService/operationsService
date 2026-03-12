package com.gpm.operations.web.rest;

import com.gpm.operations.repository.WoMotifRepository;
import com.gpm.operations.service.WoMotifService;
import com.gpm.operations.service.dto.WoMotifDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.WoMotif}.
 */
@RestController
@RequestMapping("/api")
public class WoMotifResource {

    private final Logger log = LoggerFactory.getLogger(WoMotifResource.class);

    private static final String ENTITY_NAME = "operationsServiceWoMotif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WoMotifService woMotifService;

    private final WoMotifRepository woMotifRepository;

    public WoMotifResource(WoMotifService woMotifService, WoMotifRepository woMotifRepository) {
        this.woMotifService = woMotifService;
        this.woMotifRepository = woMotifRepository;
    }

    /**
     * {@code POST  /wo-motifs} : Create a new woMotif.
     *
     * @param woMotifDTO the woMotifDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new woMotifDTO, or with status {@code 400 (Bad Request)} if the woMotif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wo-motifs")
    public ResponseEntity<WoMotifDTO> createWoMotif(@Valid @RequestBody WoMotifDTO woMotifDTO) throws URISyntaxException {
        log.debug("REST request to save WoMotif : {}", woMotifDTO);
        if (woMotifDTO.getId() != null) {
            throw new BadRequestAlertException("A new woMotif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WoMotifDTO result = woMotifService.save(woMotifDTO);
        return ResponseEntity
            .created(new URI("/api/wo-motifs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wo-motifs/:id} : Updates an existing woMotif.
     *
     * @param id the id of the woMotifDTO to save.
     * @param woMotifDTO the woMotifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated woMotifDTO,
     * or with status {@code 400 (Bad Request)} if the woMotifDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the woMotifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wo-motifs/{id}")
    public ResponseEntity<WoMotifDTO> updateWoMotif(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WoMotifDTO woMotifDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WoMotif : {}, {}", id, woMotifDTO);
        if (woMotifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, woMotifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!woMotifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WoMotifDTO result = woMotifService.update(woMotifDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woMotifDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wo-motifs/:id} : Partial updates given fields of an existing woMotif, field will ignore if it is null
     *
     * @param id the id of the woMotifDTO to save.
     * @param woMotifDTO the woMotifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated woMotifDTO,
     * or with status {@code 400 (Bad Request)} if the woMotifDTO is not valid,
     * or with status {@code 404 (Not Found)} if the woMotifDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the woMotifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wo-motifs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WoMotifDTO> partialUpdateWoMotif(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WoMotifDTO woMotifDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WoMotif partially : {}, {}", id, woMotifDTO);
        if (woMotifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, woMotifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!woMotifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WoMotifDTO> result = woMotifService.partialUpdate(woMotifDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woMotifDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wo-motifs} : get all the woMotifs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of woMotifs in body.
     */
    @GetMapping("/wo-motifs")
    public ResponseEntity<List<WoMotifDTO>> getAllWoMotifs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of WoMotifs");
        Page<WoMotifDTO> page;
        if (eagerload) {
            page = woMotifService.findAllWithEagerRelationships(pageable);
        } else {
            page = woMotifService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wo-motifs/:id} : get the "id" woMotif.
     *
     * @param id the id of the woMotifDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the woMotifDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wo-motifs/{id}")
    public ResponseEntity<WoMotifDTO> getWoMotif(@PathVariable Long id) {
        log.debug("REST request to get WoMotif : {}", id);
        Optional<WoMotifDTO> woMotifDTO = woMotifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(woMotifDTO);
    }

    /**
     * {@code DELETE  /wo-motifs/:id} : delete the "id" woMotif.
     *
     * @param id the id of the woMotifDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wo-motifs/{id}")
    public ResponseEntity<Void> deleteWoMotif(@PathVariable Long id) {
        log.debug("REST request to delete WoMotif : {}", id);
        woMotifService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
