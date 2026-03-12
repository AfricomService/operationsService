package com.gpm.operations.web.rest;

import com.gpm.operations.repository.SSTRepository;
import com.gpm.operations.service.SSTService;
import com.gpm.operations.service.dto.SSTDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.SST}.
 */
@RestController
@RequestMapping("/api")
public class SSTResource {

    private final Logger log = LoggerFactory.getLogger(SSTResource.class);

    private static final String ENTITY_NAME = "operationsServiceSst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SSTService sSTService;

    private final SSTRepository sSTRepository;

    public SSTResource(SSTService sSTService, SSTRepository sSTRepository) {
        this.sSTService = sSTService;
        this.sSTRepository = sSTRepository;
    }

    /**
     * {@code POST  /ssts} : Create a new sST.
     *
     * @param sSTDTO the sSTDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sSTDTO, or with status {@code 400 (Bad Request)} if the sST has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ssts")
    public ResponseEntity<SSTDTO> createSST(@Valid @RequestBody SSTDTO sSTDTO) throws URISyntaxException {
        log.debug("REST request to save SST : {}", sSTDTO);
        if (sSTDTO.getId() != null) {
            throw new BadRequestAlertException("A new sST cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SSTDTO result = sSTService.save(sSTDTO);
        return ResponseEntity
            .created(new URI("/api/ssts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ssts/:id} : Updates an existing sST.
     *
     * @param id the id of the sSTDTO to save.
     * @param sSTDTO the sSTDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sSTDTO,
     * or with status {@code 400 (Bad Request)} if the sSTDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sSTDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ssts/{id}")
    public ResponseEntity<SSTDTO> updateSST(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody SSTDTO sSTDTO)
        throws URISyntaxException {
        log.debug("REST request to update SST : {}, {}", id, sSTDTO);
        if (sSTDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sSTDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sSTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SSTDTO result = sSTService.update(sSTDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sSTDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ssts/:id} : Partial updates given fields of an existing sST, field will ignore if it is null
     *
     * @param id the id of the sSTDTO to save.
     * @param sSTDTO the sSTDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sSTDTO,
     * or with status {@code 400 (Bad Request)} if the sSTDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sSTDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sSTDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ssts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SSTDTO> partialUpdateSST(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SSTDTO sSTDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SST partially : {}, {}", id, sSTDTO);
        if (sSTDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sSTDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sSTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SSTDTO> result = sSTService.partialUpdate(sSTDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sSTDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ssts} : get all the sSTS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sSTS in body.
     */
    @GetMapping("/ssts")
    public ResponseEntity<List<SSTDTO>> getAllSSTS(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SSTS");
        Page<SSTDTO> page = sSTService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ssts/:id} : get the "id" sST.
     *
     * @param id the id of the sSTDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sSTDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ssts/{id}")
    public ResponseEntity<SSTDTO> getSST(@PathVariable Long id) {
        log.debug("REST request to get SST : {}", id);
        Optional<SSTDTO> sSTDTO = sSTService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sSTDTO);
    }

    /**
     * {@code DELETE  /ssts/:id} : delete the "id" sST.
     *
     * @param id the id of the sSTDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ssts/{id}")
    public ResponseEntity<Void> deleteSST(@PathVariable Long id) {
        log.debug("REST request to delete SST : {}", id);
        sSTService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
