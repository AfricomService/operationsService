package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.WorkOrderTechniciens;
import com.gpm.operations.repository.WorkOrderTechniciensRepository;
import com.gpm.operations.service.dto.WorkOrderTechniciensDTO;
import com.gpm.operations.service.mapper.WorkOrderTechniciensMapper;
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
 * Integration tests for the {@link WorkOrderTechniciensResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkOrderTechniciensResourceIT {

    private static final Long DEFAULT_WORK_ORDER_ID = 1L;
    private static final Long UPDATED_WORK_ORDER_ID = 2L;

    private static final Long DEFAULT_CONTACT_SOCIETE_ID = 1L;
    private static final Long UPDATED_CONTACT_SOCIETE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/work-order-techniciens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkOrderTechniciensRepository workOrderTechniciensRepository;

    @Autowired
    private WorkOrderTechniciensMapper workOrderTechniciensMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkOrderTechniciensMockMvc;

    private WorkOrderTechniciens workOrderTechniciens;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrderTechniciens createEntity(EntityManager em) {
        WorkOrderTechniciens workOrderTechniciens = new WorkOrderTechniciens()
            .workOrderId(DEFAULT_WORK_ORDER_ID)
            .contactSocieteId(DEFAULT_CONTACT_SOCIETE_ID);
        return workOrderTechniciens;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrderTechniciens createUpdatedEntity(EntityManager em) {
        WorkOrderTechniciens workOrderTechniciens = new WorkOrderTechniciens()
            .workOrderId(UPDATED_WORK_ORDER_ID)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID);
        return workOrderTechniciens;
    }

    @BeforeEach
    public void initTest() {
        workOrderTechniciens = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkOrderTechniciens() throws Exception {
        int databaseSizeBeforeCreate = workOrderTechniciensRepository.findAll().size();
        // Create the WorkOrderTechniciens
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(workOrderTechniciens);
        restWorkOrderTechniciensMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrderTechniciens testWorkOrderTechniciens = workOrderTechniciensList.get(workOrderTechniciensList.size() - 1);
        assertThat(testWorkOrderTechniciens.getWorkOrderId()).isEqualTo(DEFAULT_WORK_ORDER_ID);
        assertThat(testWorkOrderTechniciens.getContactSocieteId()).isEqualTo(DEFAULT_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void createWorkOrderTechniciensWithExistingId() throws Exception {
        // Create the WorkOrderTechniciens with an existing ID
        workOrderTechniciens.setId(1L);
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(workOrderTechniciens);

        int databaseSizeBeforeCreate = workOrderTechniciensRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkOrderTechniciensMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWorkOrderTechniciens() throws Exception {
        // Initialize the database
        workOrderTechniciensRepository.saveAndFlush(workOrderTechniciens);

        // Get all the workOrderTechniciensList
        restWorkOrderTechniciensMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrderTechniciens.getId().intValue())))
            .andExpect(jsonPath("$.[*].workOrderId").value(hasItem(DEFAULT_WORK_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].contactSocieteId").value(hasItem(DEFAULT_CONTACT_SOCIETE_ID.intValue())));
    }

    @Test
    @Transactional
    void getWorkOrderTechniciens() throws Exception {
        // Initialize the database
        workOrderTechniciensRepository.saveAndFlush(workOrderTechniciens);

        // Get the workOrderTechniciens
        restWorkOrderTechniciensMockMvc
            .perform(get(ENTITY_API_URL_ID, workOrderTechniciens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workOrderTechniciens.getId().intValue()))
            .andExpect(jsonPath("$.workOrderId").value(DEFAULT_WORK_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.contactSocieteId").value(DEFAULT_CONTACT_SOCIETE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingWorkOrderTechniciens() throws Exception {
        // Get the workOrderTechniciens
        restWorkOrderTechniciensMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkOrderTechniciens() throws Exception {
        // Initialize the database
        workOrderTechniciensRepository.saveAndFlush(workOrderTechniciens);

        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();

        // Update the workOrderTechniciens
        WorkOrderTechniciens updatedWorkOrderTechniciens = workOrderTechniciensRepository.findById(workOrderTechniciens.getId()).get();
        // Disconnect from session so that the updates on updatedWorkOrderTechniciens are not directly saved in db
        em.detach(updatedWorkOrderTechniciens);
        updatedWorkOrderTechniciens.workOrderId(UPDATED_WORK_ORDER_ID).contactSocieteId(UPDATED_CONTACT_SOCIETE_ID);
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(updatedWorkOrderTechniciens);

        restWorkOrderTechniciensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrderTechniciensDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderTechniciens testWorkOrderTechniciens = workOrderTechniciensList.get(workOrderTechniciensList.size() - 1);
        assertThat(testWorkOrderTechniciens.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
        assertThat(testWorkOrderTechniciens.getContactSocieteId()).isEqualTo(UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void putNonExistingWorkOrderTechniciens() throws Exception {
        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();
        workOrderTechniciens.setId(count.incrementAndGet());

        // Create the WorkOrderTechniciens
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(workOrderTechniciens);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderTechniciensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrderTechniciensDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkOrderTechniciens() throws Exception {
        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();
        workOrderTechniciens.setId(count.incrementAndGet());

        // Create the WorkOrderTechniciens
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(workOrderTechniciens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderTechniciensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkOrderTechniciens() throws Exception {
        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();
        workOrderTechniciens.setId(count.incrementAndGet());

        // Create the WorkOrderTechniciens
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(workOrderTechniciens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderTechniciensMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkOrderTechniciensWithPatch() throws Exception {
        // Initialize the database
        workOrderTechniciensRepository.saveAndFlush(workOrderTechniciens);

        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();

        // Update the workOrderTechniciens using partial update
        WorkOrderTechniciens partialUpdatedWorkOrderTechniciens = new WorkOrderTechniciens();
        partialUpdatedWorkOrderTechniciens.setId(workOrderTechniciens.getId());

        partialUpdatedWorkOrderTechniciens.workOrderId(UPDATED_WORK_ORDER_ID);

        restWorkOrderTechniciensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrderTechniciens.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrderTechniciens))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderTechniciens testWorkOrderTechniciens = workOrderTechniciensList.get(workOrderTechniciensList.size() - 1);
        assertThat(testWorkOrderTechniciens.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
        assertThat(testWorkOrderTechniciens.getContactSocieteId()).isEqualTo(DEFAULT_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void fullUpdateWorkOrderTechniciensWithPatch() throws Exception {
        // Initialize the database
        workOrderTechniciensRepository.saveAndFlush(workOrderTechniciens);

        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();

        // Update the workOrderTechniciens using partial update
        WorkOrderTechniciens partialUpdatedWorkOrderTechniciens = new WorkOrderTechniciens();
        partialUpdatedWorkOrderTechniciens.setId(workOrderTechniciens.getId());

        partialUpdatedWorkOrderTechniciens.workOrderId(UPDATED_WORK_ORDER_ID).contactSocieteId(UPDATED_CONTACT_SOCIETE_ID);

        restWorkOrderTechniciensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrderTechniciens.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrderTechniciens))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderTechniciens testWorkOrderTechniciens = workOrderTechniciensList.get(workOrderTechniciensList.size() - 1);
        assertThat(testWorkOrderTechniciens.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
        assertThat(testWorkOrderTechniciens.getContactSocieteId()).isEqualTo(UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingWorkOrderTechniciens() throws Exception {
        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();
        workOrderTechniciens.setId(count.incrementAndGet());

        // Create the WorkOrderTechniciens
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(workOrderTechniciens);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderTechniciensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workOrderTechniciensDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkOrderTechniciens() throws Exception {
        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();
        workOrderTechniciens.setId(count.incrementAndGet());

        // Create the WorkOrderTechniciens
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(workOrderTechniciens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderTechniciensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkOrderTechniciens() throws Exception {
        int databaseSizeBeforeUpdate = workOrderTechniciensRepository.findAll().size();
        workOrderTechniciens.setId(count.incrementAndGet());

        // Create the WorkOrderTechniciens
        WorkOrderTechniciensDTO workOrderTechniciensDTO = workOrderTechniciensMapper.toDto(workOrderTechniciens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderTechniciensMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderTechniciensDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrderTechniciens in the database
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkOrderTechniciens() throws Exception {
        // Initialize the database
        workOrderTechniciensRepository.saveAndFlush(workOrderTechniciens);

        int databaseSizeBeforeDelete = workOrderTechniciensRepository.findAll().size();

        // Delete the workOrderTechniciens
        restWorkOrderTechniciensMockMvc
            .perform(delete(ENTITY_API_URL_ID, workOrderTechniciens.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkOrderTechniciens> workOrderTechniciensList = workOrderTechniciensRepository.findAll();
        assertThat(workOrderTechniciensList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
