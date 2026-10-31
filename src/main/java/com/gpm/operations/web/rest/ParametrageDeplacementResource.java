package com.gpm.operations.web.rest;

import com.gpm.operations.repository.ParametrageDeplacementRepository;
import com.gpm.operations.service.ParametrageDeplacementService;
import com.gpm.operations.service.dto.ParametrageDeplacementDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.ParametrageDeplacement}.
 */
@RestController
@RequestMapping("/api")
public class ParametrageDeplacementResource {

    private final Logger log = LoggerFactory.getLogger(ParametrageDeplacementResource.class);

    private static final String ENTITY_NAME = "operationsServiceParametrageDeplacement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParametrageDeplacementService parametrageDeplacementService;

    private final ParametrageDeplacementRepository parametrageDeplacementRepository;

    public ParametrageDeplacementResource(
        ParametrageDeplacementService parametrageDeplacementService,
        ParametrageDeplacementRepository parametrageDeplacementRepository
    ) {
        this.parametrageDeplacementService = parametrageDeplacementService;
        this.parametrageDeplacementRepository = parametrageDeplacementRepository;
    }

    /**
     * {@code POST  /parametrage-deplacements} : Create a new parametrageDeplacement.
     *
     * @param parametrageDeplacementDTO the parametrageDeplacementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametrageDeplacementDTO, or with status {@code 400 (Bad Request)} if the parametrageDeplacement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parametrage-deplacements")
    public ResponseEntity<ParametrageDeplacementDTO> createParametrageDeplacement(
        @RequestBody ParametrageDeplacementDTO parametrageDeplacementDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ParametrageDeplacement : {}", parametrageDeplacementDTO);
        if (parametrageDeplacementDTO.getId() != null) {
            throw new BadRequestAlertException("A new parametrageDeplacement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParametrageDeplacementDTO result = parametrageDeplacementService.save(parametrageDeplacementDTO);
        return ResponseEntity
            .created(new URI("/api/parametrage-deplacements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parametrage-deplacements/:id} : Updates an existing parametrageDeplacement.
     *
     * @param id the id of the parametrageDeplacementDTO to save.
     * @param parametrageDeplacementDTO the parametrageDeplacementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametrageDeplacementDTO,
     * or with status {@code 400 (Bad Request)} if the parametrageDeplacementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametrageDeplacementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parametrage-deplacements/{id}")
    public ResponseEntity<ParametrageDeplacementDTO> updateParametrageDeplacement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParametrageDeplacementDTO parametrageDeplacementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ParametrageDeplacement : {}, {}", id, parametrageDeplacementDTO);
        if (parametrageDeplacementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametrageDeplacementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametrageDeplacementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParametrageDeplacementDTO result = parametrageDeplacementService.save(parametrageDeplacementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametrageDeplacementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parametrage-deplacements/:id} : Partial updates given fields of an existing parametrageDeplacement, field will ignore if it is null
     *
     * @param id the id of the parametrageDeplacementDTO to save.
     * @param parametrageDeplacementDTO the parametrageDeplacementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametrageDeplacementDTO,
     * or with status {@code 400 (Bad Request)} if the parametrageDeplacementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parametrageDeplacementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametrageDeplacementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parametrage-deplacements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParametrageDeplacementDTO> partialUpdateParametrageDeplacement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParametrageDeplacementDTO parametrageDeplacementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParametrageDeplacement partially : {}, {}", id, parametrageDeplacementDTO);
        if (parametrageDeplacementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametrageDeplacementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametrageDeplacementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParametrageDeplacementDTO> result = parametrageDeplacementService.partialUpdate(parametrageDeplacementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametrageDeplacementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parametrage-deplacements} : get all the parametrageDeplacements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametrageDeplacements in body.
     */
    @GetMapping("/parametrage-deplacements")
    public List<ParametrageDeplacementDTO> getAllParametrageDeplacements() {
        log.debug("REST request to get all ParametrageDeplacements");
        return parametrageDeplacementService.findAll();
    }

    /**
     * {@code GET  /parametrage-deplacements/work-order/:workOrderId} : get all the parametrageDeplacements of a work order.
     */
    @GetMapping("/parametrage-deplacements/work-order/{workOrderId}")
    public List<ParametrageDeplacementDTO> getParametrageDeplacementsByWorkOrder(@PathVariable Long workOrderId) {
        log.debug("REST request to get ParametrageDeplacements of WorkOrder : {}", workOrderId);
        return parametrageDeplacementService.findByWorkOrder(workOrderId);
    }

    /**
     * {@code PUT  /parametrage-deplacements/work-order/:workOrderId} : replace all the parametrageDeplacements of a work order.
     */
    @PutMapping("/parametrage-deplacements/work-order/{workOrderId}")
    public List<ParametrageDeplacementDTO> replaceParametrageDeplacementsForWorkOrder(
        @PathVariable Long workOrderId,
        @RequestBody List<ParametrageDeplacementDTO> parametrageDeplacementDTOs
    ) {
        log.debug("REST request to replace ParametrageDeplacements of WorkOrder : {}", workOrderId);
        return parametrageDeplacementService.replaceForWorkOrder(workOrderId, parametrageDeplacementDTOs);
    }

    /**
     * {@code GET  /parametrage-deplacements/:id} : get the "id" parametrageDeplacement.
     *
     * @param id the id of the parametrageDeplacementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametrageDeplacementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parametrage-deplacements/{id}")
    public ResponseEntity<ParametrageDeplacementDTO> getParametrageDeplacement(@PathVariable Long id) {
        log.debug("REST request to get ParametrageDeplacement : {}", id);
        Optional<ParametrageDeplacementDTO> parametrageDeplacementDTO = parametrageDeplacementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parametrageDeplacementDTO);
    }

    /**
     * {@code DELETE  /parametrage-deplacements/:id} : delete the "id" parametrageDeplacement.
     *
     * @param id the id of the parametrageDeplacementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parametrage-deplacements/{id}")
    public ResponseEntity<Void> deleteParametrageDeplacement(@PathVariable Long id) {
        log.debug("REST request to delete ParametrageDeplacement : {}", id);
        parametrageDeplacementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
