package com.gpm.operations.web.rest;

import com.gpm.operations.repository.MissionArticlesRepository;
import com.gpm.operations.service.MissionArticlesService;
import com.gpm.operations.service.dto.MissionArticlesDTO;
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
 * REST controller for managing {@link com.gpm.operations.domain.MissionArticles}.
 */
@RestController
@RequestMapping("/api")
public class MissionArticlesResource {

    private final Logger log = LoggerFactory.getLogger(MissionArticlesResource.class);

    private static final String ENTITY_NAME = "operationsServiceMissionArticles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MissionArticlesService missionArticlesService;

    private final MissionArticlesRepository missionArticlesRepository;

    public MissionArticlesResource(MissionArticlesService missionArticlesService, MissionArticlesRepository missionArticlesRepository) {
        this.missionArticlesService = missionArticlesService;
        this.missionArticlesRepository = missionArticlesRepository;
    }

    /**
     * {@code POST  /mission-articles} : Create a new missionArticles.
     *
     * @param missionArticlesDTO the missionArticlesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new missionArticlesDTO, or with status {@code 400 (Bad Request)} if the missionArticles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mission-articles")
    public ResponseEntity<MissionArticlesDTO> createMissionArticles(@RequestBody MissionArticlesDTO missionArticlesDTO)
        throws URISyntaxException {
        log.debug("REST request to save MissionArticles : {}", missionArticlesDTO);
        if (missionArticlesDTO.getId() != null) {
            throw new BadRequestAlertException("A new missionArticles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MissionArticlesDTO result = missionArticlesService.save(missionArticlesDTO);
        return ResponseEntity
            .created(new URI("/api/mission-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mission-articles/:id} : Updates an existing missionArticles.
     *
     * @param id the id of the missionArticlesDTO to save.
     * @param missionArticlesDTO the missionArticlesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated missionArticlesDTO,
     * or with status {@code 400 (Bad Request)} if the missionArticlesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the missionArticlesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mission-articles/{id}")
    public ResponseEntity<MissionArticlesDTO> updateMissionArticles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MissionArticlesDTO missionArticlesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MissionArticles : {}, {}", id, missionArticlesDTO);
        if (missionArticlesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, missionArticlesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!missionArticlesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MissionArticlesDTO result = missionArticlesService.update(missionArticlesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, missionArticlesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mission-articles/:id} : Partial updates given fields of an existing missionArticles, field will ignore if it is null
     *
     * @param id the id of the missionArticlesDTO to save.
     * @param missionArticlesDTO the missionArticlesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated missionArticlesDTO,
     * or with status {@code 400 (Bad Request)} if the missionArticlesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the missionArticlesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the missionArticlesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mission-articles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MissionArticlesDTO> partialUpdateMissionArticles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MissionArticlesDTO missionArticlesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MissionArticles partially : {}, {}", id, missionArticlesDTO);
        if (missionArticlesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, missionArticlesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!missionArticlesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MissionArticlesDTO> result = missionArticlesService.partialUpdate(missionArticlesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, missionArticlesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mission-articles} : get all the missionArticles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of missionArticles in body.
     */
    @GetMapping("/mission-articles")
    public List<MissionArticlesDTO> getAllMissionArticles() {
        log.debug("REST request to get all MissionArticles");
        return missionArticlesService.findAll();
    }

    /**
     * {@code GET  /mission-articles/:id} : get the "id" missionArticles.
     *
     * @param id the id of the missionArticlesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the missionArticlesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mission-articles/{id}")
    public ResponseEntity<MissionArticlesDTO> getMissionArticles(@PathVariable Long id) {
        log.debug("REST request to get MissionArticles : {}", id);
        Optional<MissionArticlesDTO> missionArticlesDTO = missionArticlesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(missionArticlesDTO);
    }

    /**
     * {@code DELETE  /mission-articles/:id} : delete the "id" missionArticles.
     *
     * @param id the id of the missionArticlesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mission-articles/{id}")
    public ResponseEntity<Void> deleteMissionArticles(@PathVariable Long id) {
        log.debug("REST request to delete MissionArticles : {}", id);
        missionArticlesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
