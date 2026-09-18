package com.gpm.operations.web.rest;

import com.gpm.operations.repository.WorkOrderVehiculeRepository;
import com.gpm.operations.service.WorkOrderVehiculeService;
import com.gpm.operations.service.dto.VehiculeConflictDTO;
import com.gpm.operations.service.dto.WorkOrderVehiculeDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.WorkOrderVehicule}.
 */
@RestController
@RequestMapping("/api")
public class WorkOrderVehiculeResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrderVehiculeResource.class);

    private static final String ENTITY_NAME = "operationsServiceWorkOrderVehicule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkOrderVehiculeService workOrderVehiculeService;

    private final WorkOrderVehiculeRepository workOrderVehiculeRepository;

    public WorkOrderVehiculeResource(
        WorkOrderVehiculeService workOrderVehiculeService,
        WorkOrderVehiculeRepository workOrderVehiculeRepository
    ) {
        this.workOrderVehiculeService = workOrderVehiculeService;
        this.workOrderVehiculeRepository = workOrderVehiculeRepository;
    }

    /**
     * {@code POST  /work-order-vehicules} : Create a new workOrderVehicule.
     *
     * @param workOrderVehiculeDTO the workOrderVehiculeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workOrderVehiculeDTO, or with status {@code 400 (Bad Request)} if the workOrderVehicule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-order-vehicules")
    public ResponseEntity<WorkOrderVehiculeDTO> createWorkOrderVehicule(@RequestBody WorkOrderVehiculeDTO workOrderVehiculeDTO)
        throws URISyntaxException {
        log.debug("REST request to save WorkOrderVehicule : {}", workOrderVehiculeDTO);
        if (workOrderVehiculeDTO.getId() != null) {
            throw new BadRequestAlertException("A new workOrderVehicule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkOrderVehiculeDTO result = workOrderVehiculeService.save(workOrderVehiculeDTO);
        return ResponseEntity
            .created(new URI("/api/work-order-vehicules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-order-vehicules/:id} : Updates an existing workOrderVehicule.
     *
     * @param id the id of the workOrderVehiculeDTO to save.
     * @param workOrderVehiculeDTO the workOrderVehiculeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderVehiculeDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderVehiculeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workOrderVehiculeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-order-vehicules/{id}")
    public ResponseEntity<WorkOrderVehiculeDTO> updateWorkOrderVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkOrderVehiculeDTO workOrderVehiculeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkOrderVehicule : {}, {}", id, workOrderVehiculeDTO);
        if (workOrderVehiculeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrderVehiculeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrderVehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkOrderVehiculeDTO result = workOrderVehiculeService.save(workOrderVehiculeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrderVehiculeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-order-vehicules/:id} : Partial updates given fields of an existing workOrderVehicule, field will ignore if it is null
     *
     * @param id the id of the workOrderVehiculeDTO to save.
     * @param workOrderVehiculeDTO the workOrderVehiculeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderVehiculeDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderVehiculeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workOrderVehiculeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workOrderVehiculeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-order-vehicules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkOrderVehiculeDTO> partialUpdateWorkOrderVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkOrderVehiculeDTO workOrderVehiculeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkOrderVehicule partially : {}, {}", id, workOrderVehiculeDTO);
        if (workOrderVehiculeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrderVehiculeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrderVehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkOrderVehiculeDTO> result = workOrderVehiculeService.partialUpdate(workOrderVehiculeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrderVehiculeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-order-vehicules} : get all the workOrderVehicules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workOrderVehicules in body.
     */
    @GetMapping("/work-order-vehicules")
    public List<WorkOrderVehiculeDTO> getAllWorkOrderVehicules() {
        log.debug("REST request to get all WorkOrderVehicules");
        return workOrderVehiculeService.findAll();
    }

    /**
     * {@code GET  /work-order-vehicules/:id} : get the "id" workOrderVehicule.
     *
     * @param id the id of the workOrderVehiculeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workOrderVehiculeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-order-vehicules/{id}")
    public ResponseEntity<WorkOrderVehiculeDTO> getWorkOrderVehicule(@PathVariable Long id) {
        log.debug("REST request to get WorkOrderVehicule : {}", id);
        Optional<WorkOrderVehiculeDTO> workOrderVehiculeDTO = workOrderVehiculeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workOrderVehiculeDTO);
    }

    /**
     * {@code GET  /work-order-vehicules/by-work-order/:workOrderId} : get all vehicules linked to a work order.
     *
     * @param workOrderId the id of the work order.
     * @return the list of linked WorkOrderVehiculeDTO.
     */
    @GetMapping("/work-order-vehicules/by-work-order/{workOrderId}")
    public List<WorkOrderVehiculeDTO> getVehiculesByWorkOrder(@PathVariable Long workOrderId) {
        log.debug("REST request to get WorkOrderVehicules by workOrder : {}", workOrderId);
        return workOrderVehiculeService.findByWorkOrderId(workOrderId);
    }

    /**
     * {@code GET /work-order-vehicules/check-disponibilite} : vérifie si des véhicules sont
     * déjà affectés à un work order en cours (dateHeureFinPrev non dépassée).
     */
    @GetMapping("/work-order-vehicules/check-disponibilite")
    public List<VehiculeConflictDTO> checkDisponibilite(
        @RequestParam List<Long> vehiculeIds,
        @RequestParam(required = false) Long excludeWorkOrderId
    ) {
        log.debug("REST request to check disponibilité : {}, exclude={}", vehiculeIds, excludeWorkOrderId);
        return workOrderVehiculeService.findConflicts(vehiculeIds, excludeWorkOrderId);
    }

    /**
     * {@code PUT  /work-order-vehicules/by-work-order/:workOrderId} : replace the full list of vehicules for a work order.
     *
     * @param workOrderId the id of the work order.
     * @param vehiculeIds the ids of the selected vehicules.
     * @return the persisted list of WorkOrderVehiculeDTO.
     */
    @PutMapping("/work-order-vehicules/by-work-order/{workOrderId}")
    public List<WorkOrderVehiculeDTO> replaceVehiculesForWorkOrder(
        @PathVariable Long workOrderId,
        @RequestBody List<Long> vehiculeIds
    ) {
        log.debug("REST request to replace WorkOrderVehicules for workOrder : {}, {}", workOrderId, vehiculeIds);
        return workOrderVehiculeService.replaceForWorkOrder(workOrderId, vehiculeIds);
    }

    /**
     * {@code DELETE  /work-order-vehicules/:id} : delete the "id" workOrderVehicule.
     *
     * @param id the id of the workOrderVehiculeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-order-vehicules/{id}")
    public ResponseEntity<Void> deleteWorkOrderVehicule(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrderVehicule : {}", id);
        workOrderVehiculeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
