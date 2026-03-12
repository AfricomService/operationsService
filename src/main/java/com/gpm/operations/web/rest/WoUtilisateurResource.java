package com.gpm.operations.web.rest;

import com.gpm.operations.repository.WoUtilisateurRepository;
import com.gpm.operations.service.WoUtilisateurService;
import com.gpm.operations.service.dto.WoUtilisateurDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.WoUtilisateur}.
 */
@RestController
@RequestMapping("/api")
public class WoUtilisateurResource {

    private final Logger log = LoggerFactory.getLogger(WoUtilisateurResource.class);

    private static final String ENTITY_NAME = "operationsServiceWoUtilisateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WoUtilisateurService woUtilisateurService;

    private final WoUtilisateurRepository woUtilisateurRepository;

    public WoUtilisateurResource(WoUtilisateurService woUtilisateurService, WoUtilisateurRepository woUtilisateurRepository) {
        this.woUtilisateurService = woUtilisateurService;
        this.woUtilisateurRepository = woUtilisateurRepository;
    }

    /**
     * {@code POST  /wo-utilisateurs} : Create a new woUtilisateur.
     *
     * @param woUtilisateurDTO the woUtilisateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new woUtilisateurDTO, or with status {@code 400 (Bad Request)} if the woUtilisateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wo-utilisateurs")
    public ResponseEntity<WoUtilisateurDTO> createWoUtilisateur(@Valid @RequestBody WoUtilisateurDTO woUtilisateurDTO)
        throws URISyntaxException {
        log.debug("REST request to save WoUtilisateur : {}", woUtilisateurDTO);
        if (woUtilisateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new woUtilisateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WoUtilisateurDTO result = woUtilisateurService.save(woUtilisateurDTO);
        return ResponseEntity
            .created(new URI("/api/wo-utilisateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wo-utilisateurs/:id} : Updates an existing woUtilisateur.
     *
     * @param id the id of the woUtilisateurDTO to save.
     * @param woUtilisateurDTO the woUtilisateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated woUtilisateurDTO,
     * or with status {@code 400 (Bad Request)} if the woUtilisateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the woUtilisateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wo-utilisateurs/{id}")
    public ResponseEntity<WoUtilisateurDTO> updateWoUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WoUtilisateurDTO woUtilisateurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WoUtilisateur : {}, {}", id, woUtilisateurDTO);
        if (woUtilisateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, woUtilisateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!woUtilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WoUtilisateurDTO result = woUtilisateurService.update(woUtilisateurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woUtilisateurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wo-utilisateurs/:id} : Partial updates given fields of an existing woUtilisateur, field will ignore if it is null
     *
     * @param id the id of the woUtilisateurDTO to save.
     * @param woUtilisateurDTO the woUtilisateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated woUtilisateurDTO,
     * or with status {@code 400 (Bad Request)} if the woUtilisateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the woUtilisateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the woUtilisateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wo-utilisateurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WoUtilisateurDTO> partialUpdateWoUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WoUtilisateurDTO woUtilisateurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WoUtilisateur partially : {}, {}", id, woUtilisateurDTO);
        if (woUtilisateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, woUtilisateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!woUtilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WoUtilisateurDTO> result = woUtilisateurService.partialUpdate(woUtilisateurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, woUtilisateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wo-utilisateurs} : get all the woUtilisateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of woUtilisateurs in body.
     */
    @GetMapping("/wo-utilisateurs")
    public ResponseEntity<List<WoUtilisateurDTO>> getAllWoUtilisateurs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WoUtilisateurs");
        Page<WoUtilisateurDTO> page = woUtilisateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wo-utilisateurs/:id} : get the "id" woUtilisateur.
     *
     * @param id the id of the woUtilisateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the woUtilisateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wo-utilisateurs/{id}")
    public ResponseEntity<WoUtilisateurDTO> getWoUtilisateur(@PathVariable Long id) {
        log.debug("REST request to get WoUtilisateur : {}", id);
        Optional<WoUtilisateurDTO> woUtilisateurDTO = woUtilisateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(woUtilisateurDTO);
    }

    /**
     * {@code DELETE  /wo-utilisateurs/:id} : delete the "id" woUtilisateur.
     *
     * @param id the id of the woUtilisateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wo-utilisateurs/{id}")
    public ResponseEntity<Void> deleteWoUtilisateur(@PathVariable Long id) {
        log.debug("REST request to delete WoUtilisateur : {}", id);
        woUtilisateurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
