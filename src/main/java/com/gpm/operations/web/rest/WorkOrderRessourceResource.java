package com.gpm.operations.web.rest;

import com.gpm.operations.repository.WorkOrderRessourceRepository;
import com.gpm.operations.service.WorkOrderRessourceService;
import com.gpm.operations.service.dto.WorkOrderRessourceDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.WorkOrderRessource}.
 */
@RestController
@RequestMapping("/api")
public class WorkOrderRessourceResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrderRessourceResource.class);

    private static final String ENTITY_NAME = "operationsServiceWorkOrderRessource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkOrderRessourceService workOrderRessourceService;

    private final WorkOrderRessourceRepository workOrderRessourceRepository;

    public WorkOrderRessourceResource(
        WorkOrderRessourceService workOrderRessourceService,
        WorkOrderRessourceRepository workOrderRessourceRepository
    ) {
        this.workOrderRessourceService = workOrderRessourceService;
        this.workOrderRessourceRepository = workOrderRessourceRepository;
    }

    /**
     * {@code POST  /work-order-ressources} : Create a new workOrderRessource.
     *
     * @param workOrderRessourceDTO the workOrderRessourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workOrderRessourceDTO, or with status {@code 400 (Bad Request)} if the workOrderRessource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-order-ressources")
    public ResponseEntity<WorkOrderRessourceDTO> createWorkOrderRessource(@RequestBody WorkOrderRessourceDTO workOrderRessourceDTO)
        throws URISyntaxException {
        log.debug("REST request to save WorkOrderRessource : {}", workOrderRessourceDTO);
        if (workOrderRessourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new workOrderRessource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkOrderRessourceDTO result = workOrderRessourceService.save(workOrderRessourceDTO);
        return ResponseEntity
            .created(new URI("/api/work-order-ressources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-order-ressources/:id} : Updates an existing workOrderRessource.
     *
     * @param id the id of the workOrderRessourceDTO to save.
     * @param workOrderRessourceDTO the workOrderRessourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderRessourceDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderRessourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workOrderRessourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-order-ressources/{id}")
    public ResponseEntity<WorkOrderRessourceDTO> updateWorkOrderRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkOrderRessourceDTO workOrderRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkOrderRessource : {}, {}", id, workOrderRessourceDTO);
        if (workOrderRessourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrderRessourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrderRessourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkOrderRessourceDTO result = workOrderRessourceService.save(workOrderRessourceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrderRessourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-order-ressources/:id} : Partial updates given fields of an existing workOrderRessource, field will ignore if it is null
     *
     * @param id the id of the workOrderRessourceDTO to save.
     * @param workOrderRessourceDTO the workOrderRessourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderRessourceDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderRessourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workOrderRessourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workOrderRessourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-order-ressources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkOrderRessourceDTO> partialUpdateWorkOrderRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkOrderRessourceDTO workOrderRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkOrderRessource partially : {}, {}", id, workOrderRessourceDTO);
        if (workOrderRessourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrderRessourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrderRessourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkOrderRessourceDTO> result = workOrderRessourceService.partialUpdate(workOrderRessourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrderRessourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-order-ressources} : get all the workOrderRessources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workOrderRessources in body.
     */
    @GetMapping("/work-order-ressources")
    public List<WorkOrderRessourceDTO> getAllWorkOrderRessources() {
        log.debug("REST request to get all WorkOrderRessources");
        return workOrderRessourceService.findAll();
    }

    /**
     * {@code GET  /work-order-ressources/:id} : get the "id" workOrderRessource.
     *
     * @param id the id of the workOrderRessourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workOrderRessourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-order-ressources/{id}")
    public ResponseEntity<WorkOrderRessourceDTO> getWorkOrderRessource(@PathVariable Long id) {
        log.debug("REST request to get WorkOrderRessource : {}", id);
        Optional<WorkOrderRessourceDTO> workOrderRessourceDTO = workOrderRessourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workOrderRessourceDTO);
    }

    /**
     * {@code DELETE  /work-order-ressources/:id} : delete the "id" workOrderRessource.
     *
     * @param id the id of the workOrderRessourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-order-ressources/{id}")
    public ResponseEntity<Void> deleteWorkOrderRessource(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrderRessource : {}", id);
        workOrderRessourceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
