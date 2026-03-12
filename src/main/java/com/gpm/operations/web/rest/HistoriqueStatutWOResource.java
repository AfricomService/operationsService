package com.gpm.operations.web.rest;

import com.gpm.operations.repository.HistoriqueStatutWORepository;
import com.gpm.operations.service.HistoriqueStatutWOService;
import com.gpm.operations.service.dto.HistoriqueStatutWODTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.HistoriqueStatutWO}.
 */
@RestController
@RequestMapping("/api")
public class HistoriqueStatutWOResource {

    private final Logger log = LoggerFactory.getLogger(HistoriqueStatutWOResource.class);

    private static final String ENTITY_NAME = "operationsServiceHistoriqueStatutWo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueStatutWOService historiqueStatutWOService;

    private final HistoriqueStatutWORepository historiqueStatutWORepository;

    public HistoriqueStatutWOResource(
        HistoriqueStatutWOService historiqueStatutWOService,
        HistoriqueStatutWORepository historiqueStatutWORepository
    ) {
        this.historiqueStatutWOService = historiqueStatutWOService;
        this.historiqueStatutWORepository = historiqueStatutWORepository;
    }

    /**
     * {@code POST  /historique-statut-wos} : Create a new historiqueStatutWO.
     *
     * @param historiqueStatutWODTO the historiqueStatutWODTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueStatutWODTO, or with status {@code 400 (Bad Request)} if the historiqueStatutWO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historique-statut-wos")
    public ResponseEntity<HistoriqueStatutWODTO> createHistoriqueStatutWO(@Valid @RequestBody HistoriqueStatutWODTO historiqueStatutWODTO)
        throws URISyntaxException {
        log.debug("REST request to save HistoriqueStatutWO : {}", historiqueStatutWODTO);
        if (historiqueStatutWODTO.getId() != null) {
            throw new BadRequestAlertException("A new historiqueStatutWO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoriqueStatutWODTO result = historiqueStatutWOService.save(historiqueStatutWODTO);
        return ResponseEntity
            .created(new URI("/api/historique-statut-wos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historique-statut-wos/:id} : Updates an existing historiqueStatutWO.
     *
     * @param id the id of the historiqueStatutWODTO to save.
     * @param historiqueStatutWODTO the historiqueStatutWODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueStatutWODTO,
     * or with status {@code 400 (Bad Request)} if the historiqueStatutWODTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueStatutWODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historique-statut-wos/{id}")
    public ResponseEntity<HistoriqueStatutWODTO> updateHistoriqueStatutWO(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoriqueStatutWODTO historiqueStatutWODTO
    ) throws URISyntaxException {
        log.debug("REST request to update HistoriqueStatutWO : {}, {}", id, historiqueStatutWODTO);
        if (historiqueStatutWODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueStatutWODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueStatutWORepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistoriqueStatutWODTO result = historiqueStatutWOService.update(historiqueStatutWODTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueStatutWODTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /historique-statut-wos/:id} : Partial updates given fields of an existing historiqueStatutWO, field will ignore if it is null
     *
     * @param id the id of the historiqueStatutWODTO to save.
     * @param historiqueStatutWODTO the historiqueStatutWODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueStatutWODTO,
     * or with status {@code 400 (Bad Request)} if the historiqueStatutWODTO is not valid,
     * or with status {@code 404 (Not Found)} if the historiqueStatutWODTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiqueStatutWODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/historique-statut-wos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoriqueStatutWODTO> partialUpdateHistoriqueStatutWO(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoriqueStatutWODTO historiqueStatutWODTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoriqueStatutWO partially : {}, {}", id, historiqueStatutWODTO);
        if (historiqueStatutWODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueStatutWODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueStatutWORepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoriqueStatutWODTO> result = historiqueStatutWOService.partialUpdate(historiqueStatutWODTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueStatutWODTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historique-statut-wos} : get all the historiqueStatutWOS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueStatutWOS in body.
     */
    @GetMapping("/historique-statut-wos")
    public ResponseEntity<List<HistoriqueStatutWODTO>> getAllHistoriqueStatutWOS(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of HistoriqueStatutWOS");
        Page<HistoriqueStatutWODTO> page = historiqueStatutWOService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-statut-wos/:id} : get the "id" historiqueStatutWO.
     *
     * @param id the id of the historiqueStatutWODTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueStatutWODTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historique-statut-wos/{id}")
    public ResponseEntity<HistoriqueStatutWODTO> getHistoriqueStatutWO(@PathVariable Long id) {
        log.debug("REST request to get HistoriqueStatutWO : {}", id);
        Optional<HistoriqueStatutWODTO> historiqueStatutWODTO = historiqueStatutWOService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueStatutWODTO);
    }

    /**
     * {@code DELETE  /historique-statut-wos/:id} : delete the "id" historiqueStatutWO.
     *
     * @param id the id of the historiqueStatutWODTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historique-statut-wos/{id}")
    public ResponseEntity<Void> deleteHistoriqueStatutWO(@PathVariable Long id) {
        log.debug("REST request to delete HistoriqueStatutWO : {}", id);
        historiqueStatutWOService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
