package com.gpm.operations.web.rest;

import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.domain.enumeration.WorkOrderEvent;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.service.WorkOrderService;
import com.gpm.operations.service.WorkOrderStateService;
import com.gpm.operations.service.dto.WorkOrderDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.WorkOrder}.
 */
@RestController
@RequestMapping("/api")
public class WorkOrderResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrderResource.class);

    private static final String ENTITY_NAME = "operationsServiceWorkOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkOrderService workOrderService;

    private final WorkOrderRepository workOrderRepository;

    private final WorkOrderStateService workOrderStateService;

    public WorkOrderResource(
        WorkOrderService workOrderService,
        WorkOrderRepository workOrderRepository,
        WorkOrderStateService workOrderStateService
    ) {
        this.workOrderService = workOrderService;
        this.workOrderRepository = workOrderRepository;
        this.workOrderStateService = workOrderStateService;
    }

    /**
     * {@code POST  /work-orders} : Create a new workOrder.
     *
     * @param workOrderDTO the workOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workOrderDTO, or with status {@code 400 (Bad Request)} if the workOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-orders")
    public ResponseEntity<WorkOrderDTO> createWorkOrder(@Valid @RequestBody WorkOrderDTO workOrderDTO) throws URISyntaxException {
        log.debug("REST request to save WorkOrder : {}", workOrderDTO);
        if (workOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new workOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkOrderDTO result = workOrderService.save(workOrderDTO);
        return ResponseEntity
            .created(new URI("/api/work-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-orders/:id} : Updates an existing workOrder.
     *
     * @param id the id of the workOrderDTO to save.
     * @param workOrderDTO the workOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-orders/{id}")
    public ResponseEntity<WorkOrderDTO> updateWorkOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkOrderDTO workOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkOrder : {}, {}", id, workOrderDTO);
        if (workOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkOrderDTO result = workOrderService.update(workOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-orders/:id} : Partial updates given fields of an existing workOrder, field will ignore if it is null
     *
     * @param id the id of the workOrderDTO to save.
     * @param workOrderDTO the workOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkOrderDTO> partialUpdateWorkOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkOrderDTO workOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkOrder partially : {}, {}", id, workOrderDTO);
        if (workOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkOrderDTO> result = workOrderService.partialUpdate(workOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-orders} : get all the workOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workOrders in body.
     */
    @GetMapping("/work-orders")
    public ResponseEntity<List<WorkOrderDTO>> getAllWorkOrders(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WorkOrders");
        Page<WorkOrderDTO> page = workOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-orders/:id} : get the "id" workOrder.
     *
     * @param id the id of the workOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-orders/{id}")
    public ResponseEntity<WorkOrderDTO> getWorkOrder(@PathVariable Long id) {
        log.debug("REST request to get WorkOrder : {}", id);
        Optional<WorkOrderDTO> workOrderDTO = workOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workOrderDTO);
    }

    /**
     * {@code DELETE  /work-orders/:id} : delete the "id" workOrder.
     *
     * @param id the id of the workOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-orders/{id}")
    public ResponseEntity<Void> deleteWorkOrder(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrder : {}", id);
        workOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /work-orders/:id}/transition/:event : Trigger a state transition for an existing workOrder.
     *
     * @param id the id of the workOrder to trigger event for.
     * @param event the event to trigger.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new status.
     */
    @PostMapping("/work-orders/{id}/transition/{event}")
    public ResponseEntity<StatutWO> triggerTransition(@PathVariable Long id, @PathVariable WorkOrderEvent event) {
        log.debug("REST request to trigger transition {} on WorkOrder : {}", event, id);
        try {
            StatutWO newStatus = workOrderStateService.changeStatus(id, event);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlert(applicationName, "WorkOrder status changed to " + newStatus, id.toString()))
                .body(newStatus);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidtransition");
        }
    }
}
