package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.WorkOrderRessource;
import com.gpm.operations.repository.WorkOrderRessourceRepository;
import com.gpm.operations.service.dto.WorkOrderRessourceDTO;
import com.gpm.operations.service.mapper.WorkOrderRessourceMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkOrderRessourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkOrderRessourceResourceIT {

    private static final Long DEFAULT_WORK_ORDER_ID = 1L;
    private static final Long UPDATED_WORK_ORDER_ID = 2L;

    private static final Long DEFAULT_RESSOURCE_ID = 1L;
    private static final Long UPDATED_RESSOURCE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/work-order-ressources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkOrderRessourceRepository workOrderRessourceRepository;

    @Autowired
    private WorkOrderRessourceMapper workOrderRessourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkOrderRessourceMockMvc;

    private WorkOrderRessource workOrderRessource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrderRessource createEntity(EntityManager em) {
        WorkOrderRessource workOrderRessource = new WorkOrderRessource()
            .workOrderId(DEFAULT_WORK_ORDER_ID)
            .ressourceId(DEFAULT_RESSOURCE_ID);
        return workOrderRessource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrderRessource createUpdatedEntity(EntityManager em) {
        WorkOrderRessource workOrderRessource = new WorkOrderRessource()
            .workOrderId(UPDATED_WORK_ORDER_ID)
            .ressourceId(UPDATED_RESSOURCE_ID);
        return workOrderRessource;
    }

    @BeforeEach
    public void initTest() {
        workOrderRessource = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkOrderRessource() throws Exception {
        int databaseSizeBeforeCreate = workOrderRessourceRepository.findAll().size();
        // Create the WorkOrderRessource
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(workOrderRessource);
        restWorkOrderRessourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrderRessource testWorkOrderRessource = workOrderRessourceList.get(workOrderRessourceList.size() - 1);
        assertThat(testWorkOrderRessource.getWorkOrderId()).isEqualTo(DEFAULT_WORK_ORDER_ID);
        assertThat(testWorkOrderRessource.getRessourceId()).isEqualTo(DEFAULT_RESSOURCE_ID);
    }

    @Test
    @Transactional
    void createWorkOrderRessourceWithExistingId() throws Exception {
        // Create the WorkOrderRessource with an existing ID
        workOrderRessource.setId(1L);
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(workOrderRessource);

        int databaseSizeBeforeCreate = workOrderRessourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkOrderRessourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWorkOrderRessources() throws Exception {
        // Initialize the database
        workOrderRessourceRepository.saveAndFlush(workOrderRessource);

        // Get all the workOrderRessourceList
        restWorkOrderRessourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrderRessource.getId().intValue())))
            .andExpect(jsonPath("$.[*].workOrderId").value(hasItem(DEFAULT_WORK_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].ressourceId").value(hasItem(DEFAULT_RESSOURCE_ID.intValue())));
    }

    @Test
    @Transactional
    void getWorkOrderRessource() throws Exception {
        // Initialize the database
        workOrderRessourceRepository.saveAndFlush(workOrderRessource);

        // Get the workOrderRessource
        restWorkOrderRessourceMockMvc
            .perform(get(ENTITY_API_URL_ID, workOrderRessource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workOrderRessource.getId().intValue()))
            .andExpect(jsonPath("$.workOrderId").value(DEFAULT_WORK_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.ressourceId").value(DEFAULT_RESSOURCE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingWorkOrderRessource() throws Exception {
        // Get the workOrderRessource
        restWorkOrderRessourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkOrderRessource() throws Exception {
        // Initialize the database
        workOrderRessourceRepository.saveAndFlush(workOrderRessource);

        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();

        // Update the workOrderRessource
        WorkOrderRessource updatedWorkOrderRessource = workOrderRessourceRepository.findById(workOrderRessource.getId()).get();
        // Disconnect from session so that the updates on updatedWorkOrderRessource are not directly saved in db
        em.detach(updatedWorkOrderRessource);
        updatedWorkOrderRessource.workOrderId(UPDATED_WORK_ORDER_ID).ressourceId(UPDATED_RESSOURCE_ID);
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(updatedWorkOrderRessource);

        restWorkOrderRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrderRessourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderRessource testWorkOrderRessource = workOrderRessourceList.get(workOrderRessourceList.size() - 1);
        assertThat(testWorkOrderRessource.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
        assertThat(testWorkOrderRessource.getRessourceId()).isEqualTo(UPDATED_RESSOURCE_ID);
    }

    @Test
    @Transactional
    void putNonExistingWorkOrderRessource() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();
        workOrderRessource.setId(count.incrementAndGet());

        // Create the WorkOrderRessource
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(workOrderRessource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrderRessourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkOrderRessource() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();
        workOrderRessource.setId(count.incrementAndGet());

        // Create the WorkOrderRessource
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(workOrderRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkOrderRessource() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();
        workOrderRessource.setId(count.incrementAndGet());

        // Create the WorkOrderRessource
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(workOrderRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderRessourceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkOrderRessourceWithPatch() throws Exception {
        // Initialize the database
        workOrderRessourceRepository.saveAndFlush(workOrderRessource);

        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();

        // Update the workOrderRessource using partial update
        WorkOrderRessource partialUpdatedWorkOrderRessource = new WorkOrderRessource();
        partialUpdatedWorkOrderRessource.setId(workOrderRessource.getId());

        partialUpdatedWorkOrderRessource.workOrderId(UPDATED_WORK_ORDER_ID);

        restWorkOrderRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrderRessource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrderRessource))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderRessource testWorkOrderRessource = workOrderRessourceList.get(workOrderRessourceList.size() - 1);
        assertThat(testWorkOrderRessource.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
        assertThat(testWorkOrderRessource.getRessourceId()).isEqualTo(DEFAULT_RESSOURCE_ID);
    }

    @Test
    @Transactional
    void fullUpdateWorkOrderRessourceWithPatch() throws Exception {
        // Initialize the database
        workOrderRessourceRepository.saveAndFlush(workOrderRessource);

        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();

        // Update the workOrderRessource using partial update
        WorkOrderRessource partialUpdatedWorkOrderRessource = new WorkOrderRessource();
        partialUpdatedWorkOrderRessource.setId(workOrderRessource.getId());

        partialUpdatedWorkOrderRessource.workOrderId(UPDATED_WORK_ORDER_ID).ressourceId(UPDATED_RESSOURCE_ID);

        restWorkOrderRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrderRessource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrderRessource))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderRessource testWorkOrderRessource = workOrderRessourceList.get(workOrderRessourceList.size() - 1);
        assertThat(testWorkOrderRessource.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
        assertThat(testWorkOrderRessource.getRessourceId()).isEqualTo(UPDATED_RESSOURCE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingWorkOrderRessource() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();
        workOrderRessource.setId(count.incrementAndGet());

        // Create the WorkOrderRessource
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(workOrderRessource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workOrderRessourceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkOrderRessource() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();
        workOrderRessource.setId(count.incrementAndGet());

        // Create the WorkOrderRessource
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(workOrderRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkOrderRessource() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRessourceRepository.findAll().size();
        workOrderRessource.setId(count.incrementAndGet());

        // Create the WorkOrderRessource
        WorkOrderRessourceDTO workOrderRessourceDTO = workOrderRessourceMapper.toDto(workOrderRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderRessourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrderRessource in the database
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkOrderRessource() throws Exception {
        // Initialize the database
        workOrderRessourceRepository.saveAndFlush(workOrderRessource);

        int databaseSizeBeforeDelete = workOrderRessourceRepository.findAll().size();

        // Delete the workOrderRessource
        restWorkOrderRessourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, workOrderRessource.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkOrderRessource> workOrderRessourceList = workOrderRessourceRepository.findAll();
        assertThat(workOrderRessourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
