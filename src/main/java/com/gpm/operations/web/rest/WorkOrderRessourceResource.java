package com.gpm.operations.web.rest;

import com.gpm.operations.repository.WorkOrderRessourceRepository;
import com.gpm.operations.service.WorkOrderRessourceService;
import com.gpm.operations.service.dto.RessourceConflictDTO;
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

    @GetMapping("/work-order-ressources")
    public List<WorkOrderRessourceDTO> getAllWorkOrderRessources() {
        log.debug("REST request to get all WorkOrderRessources");
        return workOrderRessourceService.findAll();
    }

    @GetMapping("/work-order-ressources/{id}")
    public ResponseEntity<WorkOrderRessourceDTO> getWorkOrderRessource(@PathVariable Long id) {
        log.debug("REST request to get WorkOrderRessource : {}", id);
        Optional<WorkOrderRessourceDTO> workOrderRessourceDTO = workOrderRessourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workOrderRessourceDTO);
    }

    /**
     * {@code GET /work-order-ressources/by-work-order/:workOrderId} : get all ressources linked to a work order.
     */
    @GetMapping("/work-order-ressources/by-work-order/{workOrderId}")
    public List<WorkOrderRessourceDTO> getRessourcesByWorkOrder(@PathVariable Long workOrderId) {
        log.debug("REST request to get WorkOrderRessources by workOrder : {}", workOrderId);
        return workOrderRessourceService.findByWorkOrderId(workOrderId);
    }

    /**
     * {@code GET /work-order-ressources/check-disponibilite} : vérifie si des ressources sont
     * déjà affectées à un work order en cours (dateHeureFinPrev non dépassée).
     */
    @GetMapping("/work-order-ressources/check-disponibilite")
    public List<RessourceConflictDTO> checkDisponibilite(
        @RequestParam List<Long> ressourceIds,
        @RequestParam(required = false) Long excludeWorkOrderId
    ) {
        log.debug("REST request to check disponibilité : {}, exclude={}", ressourceIds, excludeWorkOrderId);
        return workOrderRessourceService.findConflicts(ressourceIds, excludeWorkOrderId);
    }

    /**
     * {@code PUT /work-order-ressources/by-work-order/:workOrderId} : replace the full list of ressources for a work order.
     */
    @PutMapping("/work-order-ressources/by-work-order/{workOrderId}")
    public List<WorkOrderRessourceDTO> replaceRessourcesForWorkOrder(
        @PathVariable Long workOrderId,
        @RequestBody List<Long> ressourceIds
    ) {
        log.debug("REST request to replace WorkOrderRessources for workOrder : {}, {}", workOrderId, ressourceIds);
        return workOrderRessourceService.replaceForWorkOrder(workOrderId, ressourceIds);
    }

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
