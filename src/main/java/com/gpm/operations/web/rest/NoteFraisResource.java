package com.gpm.operations.web.rest;

import com.gpm.operations.repository.NoteFraisRepository;
import com.gpm.operations.service.NoteFraisService;
import com.gpm.operations.service.dto.NoteFraisDTO;
import com.gpm.operations.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.gpm.operations.domain.NoteFrais}.
 */
@RestController
@RequestMapping("/api")
public class NoteFraisResource {

    private final Logger log = LoggerFactory.getLogger(NoteFraisResource.class);

    private static final String ENTITY_NAME = "operationsServiceNoteFrais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoteFraisService noteFraisService;

    private final NoteFraisRepository noteFraisRepository;

    public NoteFraisResource(NoteFraisService noteFraisService, NoteFraisRepository noteFraisRepository) {
        this.noteFraisService = noteFraisService;
        this.noteFraisRepository = noteFraisRepository;
    }

    /**
     * {@code POST  /note-frais} : Create a new noteFrais.
     *
     * @param noteFraisDTO the noteFraisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noteFraisDTO, or with status {@code 400 (Bad Request)} if the noteFrais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/note-frais")
    public ResponseEntity<NoteFraisDTO> createNoteFrais(@RequestBody NoteFraisDTO noteFraisDTO) throws URISyntaxException {
        log.debug("REST request to save NoteFrais : {}", noteFraisDTO);
        if (noteFraisDTO.getId() != null) {
            throw new BadRequestAlertException("A new noteFrais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NoteFraisDTO result = noteFraisService.save(noteFraisDTO);
        return ResponseEntity
            .created(new URI("/api/note-frais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /note-frais/:id} : Updates an existing noteFrais.
     *
     * @param id the id of the noteFraisDTO to save.
     * @param noteFraisDTO the noteFraisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteFraisDTO,
     * or with status {@code 400 (Bad Request)} if the noteFraisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noteFraisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/note-frais/{id}")
    public ResponseEntity<NoteFraisDTO> updateNoteFrais(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NoteFraisDTO noteFraisDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NoteFrais : {}, {}", id, noteFraisDTO);
        if (noteFraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noteFraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteFraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NoteFraisDTO result = noteFraisService.save(noteFraisDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteFraisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /note-frais/:id} : Partial updates given fields of an existing noteFrais, field will ignore if it is null
     *
     * @param id the id of the noteFraisDTO to save.
     * @param noteFraisDTO the noteFraisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteFraisDTO,
     * or with status {@code 400 (Bad Request)} if the noteFraisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the noteFraisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the noteFraisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/note-frais/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NoteFraisDTO> partialUpdateNoteFrais(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NoteFraisDTO noteFraisDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NoteFrais partially : {}, {}", id, noteFraisDTO);
        if (noteFraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noteFraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteFraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NoteFraisDTO> result = noteFraisService.partialUpdate(noteFraisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteFraisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /note-frais} : get all the noteFrais.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of noteFrais in body.
     */
    @GetMapping("/note-frais")
    public ResponseEntity<List<NoteFraisDTO>> getAllNoteFrais(Pageable pageable) {
        log.debug("REST request to get a page of NoteFrais");
        Page<NoteFraisDTO> page = noteFraisService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /note-frais/:id} : get the "id" noteFrais.
     *
     * @param id the id of the noteFraisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noteFraisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/note-frais/{id}")
    public ResponseEntity<NoteFraisDTO> getNoteFrais(@PathVariable Long id) {
        log.debug("REST request to get NoteFrais : {}", id);
        Optional<NoteFraisDTO> noteFraisDTO = noteFraisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(noteFraisDTO);
    }

    /**
     * {@code DELETE  /note-frais/:id} : delete the "id" noteFrais.
     *
     * @param id the id of the noteFraisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/note-frais/{id}")
    public ResponseEntity<Void> deleteNoteFrais(@PathVariable Long id) {
        log.debug("REST request to delete NoteFrais : {}", id);
        noteFraisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
