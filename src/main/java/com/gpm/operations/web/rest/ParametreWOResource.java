package com.gpm.operations.web.rest;

import com.gpm.operations.repository.ParametreWORepository;
import com.gpm.operations.service.ParametreWOService;
import com.gpm.operations.service.dto.ParametreWODTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.ParametreWO}.
 */
@RestController
@RequestMapping("/api")
public class ParametreWOResource {

    private final Logger log = LoggerFactory.getLogger(ParametreWOResource.class);

    private static final String ENTITY_NAME = "operationsServiceParametreWo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParametreWOService parametreWOService;

    private final ParametreWORepository parametreWORepository;

    public ParametreWOResource(ParametreWOService parametreWOService, ParametreWORepository parametreWORepository) {
        this.parametreWOService = parametreWOService;
        this.parametreWORepository = parametreWORepository;
    }

    /**
     * {@code POST  /parametre-wos} : Create a new parametreWO.
     *
     * @param parametreWODTO the parametreWODTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametreWODTO, or with status {@code 400 (Bad Request)} if the parametreWO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parametre-wos")
    public ResponseEntity<ParametreWODTO> createParametreWO(@RequestBody ParametreWODTO parametreWODTO) throws URISyntaxException {
        log.debug("REST request to save ParametreWO : {}", parametreWODTO);
        if (parametreWODTO.getId() != null) {
            throw new BadRequestAlertException("A new parametreWO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParametreWODTO result = parametreWOService.save(parametreWODTO);
        return ResponseEntity
            .created(new URI("/api/parametre-wos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parametre-wos/:id} : Updates an existing parametreWO.
     *
     * @param id the id of the parametreWODTO to save.
     * @param parametreWODTO the parametreWODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametreWODTO,
     * or with status {@code 400 (Bad Request)} if the parametreWODTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametreWODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parametre-wos/{id}")
    public ResponseEntity<ParametreWODTO> updateParametreWO(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParametreWODTO parametreWODTO
    ) throws URISyntaxException {
        log.debug("REST request to update ParametreWO : {}, {}", id, parametreWODTO);
        if (parametreWODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametreWODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametreWORepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParametreWODTO result = parametreWOService.save(parametreWODTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametreWODTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parametre-wos/:id} : Partial updates given fields of an existing parametreWO, field will ignore if it is null
     *
     * @param id the id of the parametreWODTO to save.
     * @param parametreWODTO the parametreWODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametreWODTO,
     * or with status {@code 400 (Bad Request)} if the parametreWODTO is not valid,
     * or with status {@code 404 (Not Found)} if the parametreWODTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametreWODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parametre-wos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParametreWODTO> partialUpdateParametreWO(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParametreWODTO parametreWODTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParametreWO partially : {}, {}", id, parametreWODTO);
        if (parametreWODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametreWODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametreWORepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParametreWODTO> result = parametreWOService.partialUpdate(parametreWODTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametreWODTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parametre-wos} : get all the parametreWOS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametreWOS in body.
     */
    @GetMapping("/parametre-wos")
    public List<ParametreWODTO> getAllParametreWOS() {
        log.debug("REST request to get all ParametreWOS");
        return parametreWOService.findAll();
    }

    /**
     * {@code GET  /parametre-wos/:id} : get the "id" parametreWO.
     *
     * @param id the id of the parametreWODTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametreWODTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parametre-wos/{id}")
    public ResponseEntity<ParametreWODTO> getParametreWO(@PathVariable Long id) {
        log.debug("REST request to get ParametreWO : {}", id);
        Optional<ParametreWODTO> parametreWODTO = parametreWOService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parametreWODTO);
    }

    /**
     * {@code DELETE  /parametre-wos/:id} : delete the "id" parametreWO.
     *
     * @param id the id of the parametreWODTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parametre-wos/{id}")
    public ResponseEntity<Void> deleteParametreWO(@PathVariable Long id) {
        log.debug("REST request to delete ParametreWO : {}", id);
        parametreWOService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
