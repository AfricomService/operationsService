package com.gpm.operations.web.rest;

import com.gpm.operations.repository.WorkOrderTechniciensRepository;
import com.gpm.operations.service.WorkOrderTechniciensService;
import com.gpm.operations.service.dto.WorkOrderTechniciensDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.WorkOrderTechniciens}.
 */
@RestController
@RequestMapping("/api")
public class WorkOrderTechniciensResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrderTechniciensResource.class);

    private static final String ENTITY_NAME = "operationsServiceWorkOrderTechniciens";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkOrderTechniciensService workOrderTechniciensService;

    private final WorkOrderTechniciensRepository workOrderTechniciensRepository;

    public WorkOrderTechniciensResource(
        WorkOrderTechniciensService workOrderTechniciensService,
        WorkOrderTechniciensRepository workOrderTechniciensRepository
    ) {
        this.workOrderTechniciensService = workOrderTechniciensService;
        this.workOrderTechniciensRepository = workOrderTechniciensRepository;
    }

    /**
     * {@code POST  /work-order-techniciens} : Create a new workOrderTechniciens.
     *
     * @param workOrderTechniciensDTO the workOrderTechniciensDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workOrderTechniciensDTO, or with status {@code 400 (Bad Request)} if the workOrderTechniciens has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-order-techniciens")
    public ResponseEntity<WorkOrderTechniciensDTO> createWorkOrderTechniciens(@RequestBody WorkOrderTechniciensDTO workOrderTechniciensDTO)
        throws URISyntaxException {
        log.debug("REST request to save WorkOrderTechniciens : {}", workOrderTechniciensDTO);
        if (workOrderTechniciensDTO.getId() != null) {
            throw new BadRequestAlertException("A new workOrderTechniciens cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkOrderTechniciensDTO result = workOrderTechniciensService.save(workOrderTechniciensDTO);
        return ResponseEntity
            .created(new URI("/api/work-order-techniciens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-order-techniciens/:id} : Updates an existing workOrderTechniciens.
     *
     * @param id the id of the workOrderTechniciensDTO to save.
     * @param workOrderTechniciensDTO the workOrderTechniciensDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderTechniciensDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderTechniciensDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workOrderTechniciensDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-order-techniciens/{id}")
    public ResponseEntity<WorkOrderTechniciensDTO> updateWorkOrderTechniciens(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkOrderTechniciensDTO workOrderTechniciensDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkOrderTechniciens : {}, {}", id, workOrderTechniciensDTO);
        if (workOrderTechniciensDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrderTechniciensDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrderTechniciensRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkOrderTechniciensDTO result = workOrderTechniciensService.save(workOrderTechniciensDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrderTechniciensDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-order-techniciens/:id} : Partial updates given fields of an existing workOrderTechniciens, field will ignore if it is null
     *
     * @param id the id of the workOrderTechniciensDTO to save.
     * @param workOrderTechniciensDTO the workOrderTechniciensDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderTechniciensDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderTechniciensDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workOrderTechniciensDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workOrderTechniciensDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-order-techniciens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkOrderTechniciensDTO> partialUpdateWorkOrderTechniciens(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkOrderTechniciensDTO workOrderTechniciensDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkOrderTechniciens partially : {}, {}", id, workOrderTechniciensDTO);
        if (workOrderTechniciensDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrderTechniciensDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrderTechniciensRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkOrderTechniciensDTO> result = workOrderTechniciensService.partialUpdate(workOrderTechniciensDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrderTechniciensDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-order-techniciens} : get all the workOrderTechniciens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workOrderTechniciens in body.
     */
    @GetMapping("/work-order-techniciens")
    public List<WorkOrderTechniciensDTO> getAllWorkOrderTechniciens() {
        log.debug("REST request to get all WorkOrderTechniciens");
        return workOrderTechniciensService.findAll();
    }

    /**
     * {@code GET  /work-order-techniciens/:id} : get the "id" workOrderTechniciens.
     *
     * @param id the id of the workOrderTechniciensDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workOrderTechniciensDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-order-techniciens/{id}")
    public ResponseEntity<WorkOrderTechniciensDTO> getWorkOrderTechniciens(@PathVariable Long id) {
        log.debug("REST request to get WorkOrderTechniciens : {}", id);
        Optional<WorkOrderTechniciensDTO> workOrderTechniciensDTO = workOrderTechniciensService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workOrderTechniciensDTO);
    }

    /**
     * {@code GET  /work-order-techniciens/by-work-order/:workOrderId} : get all techniciens linked to a work order.
     *
     * @param workOrderId the id of the work order.
     * @return the list of linked WorkOrderTechniciensDTO.
     */
    @GetMapping("/work-order-techniciens/by-work-order/{workOrderId}")
    public List<WorkOrderTechniciensDTO> getTechniciensByWorkOrder(@PathVariable Long workOrderId) {
        log.debug("REST request to get WorkOrderTechniciens by workOrder : {}", workOrderId);
        return workOrderTechniciensService.findByWorkOrderId(workOrderId);
    }

    /**
     * {@code PUT  /work-order-techniciens/by-work-order/:workOrderId} : replace the full list of techniciens for a work order.
     *
     * @param workOrderId the id of the work order.
     * @param contactSocieteIds the ids of the selected contacts (role TECHNIQUE).
     * @return the persisted list of WorkOrderTechniciensDTO.
     */
    @PutMapping("/work-order-techniciens/by-work-order/{workOrderId}")
    public List<WorkOrderTechniciensDTO> replaceTechniciensForWorkOrder(
        @PathVariable Long workOrderId,
        @RequestBody List<Long> contactSocieteIds
    ) {
        log.debug("REST request to replace WorkOrderTechniciens for workOrder : {}, {}", workOrderId, contactSocieteIds);
        return workOrderTechniciensService.replaceForWorkOrder(workOrderId, contactSocieteIds);
    }

    /**
     * {@code DELETE  /work-order-techniciens/:id} : delete the "id" workOrderTechniciens.
     *
     * @param id the id of the workOrderTechniciensDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-order-techniciens/{id}")
    public ResponseEntity<Void> deleteWorkOrderTechniciens(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrderTechniciens : {}", id);
        workOrderTechniciensService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
