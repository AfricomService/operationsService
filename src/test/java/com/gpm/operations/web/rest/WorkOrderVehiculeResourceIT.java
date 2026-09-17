package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.WorkOrderVehicule;
import com.gpm.operations.repository.WorkOrderVehiculeRepository;
import com.gpm.operations.service.dto.WorkOrderVehiculeDTO;
import com.gpm.operations.service.mapper.WorkOrderVehiculeMapper;
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
 * Integration tests for the {@link WorkOrderVehiculeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkOrderVehiculeResourceIT {

    private static final Long DEFAULT_WORK_ORDER_ID = 1L;
    private static final Long UPDATED_WORK_ORDER_ID = 2L;

    private static final Long DEFAULT_VEHICULE_ID = 1L;
    private static final Long UPDATED_VEHICULE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/work-order-vehicules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkOrderVehiculeRepository workOrderVehiculeRepository;

    @Autowired
    private WorkOrderVehiculeMapper workOrderVehiculeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkOrderVehiculeMockMvc;

    private WorkOrderVehicule workOrderVehicule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrderVehicule createEntity(EntityManager em) {
        WorkOrderVehicule workOrderVehicule = new WorkOrderVehicule().workOrderId(DEFAULT_WORK_ORDER_ID).vehiculeId(DEFAULT_VEHICULE_ID);
        return workOrderVehicule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrderVehicule createUpdatedEntity(EntityManager em) {
        WorkOrderVehicule workOrderVehicule = new WorkOrderVehicule().workOrderId(UPDATED_WORK_ORDER_ID).vehiculeId(UPDATED_VEHICULE_ID);
        return workOrderVehicule;
    }

    @BeforeEach
    public void initTest() {
        workOrderVehicule = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkOrderVehicule() throws Exception {
        int databaseSizeBeforeCreate = workOrderVehiculeRepository.findAll().size();
        // Create the WorkOrderVehicule
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(workOrderVehicule);
        restWorkOrderVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrderVehicule testWorkOrderVehicule = workOrderVehiculeList.get(workOrderVehiculeList.size() - 1);
        assertThat(testWorkOrderVehicule.getWorkOrderId()).isEqualTo(DEFAULT_WORK_ORDER_ID);
        assertThat(testWorkOrderVehicule.getVehiculeId()).isEqualTo(DEFAULT_VEHICULE_ID);
    }

    @Test
    @Transactional
    void createWorkOrderVehiculeWithExistingId() throws Exception {
        // Create the WorkOrderVehicule with an existing ID
        workOrderVehicule.setId(1L);
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(workOrderVehicule);

        int databaseSizeBeforeCreate = workOrderVehiculeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkOrderVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWorkOrderVehicules() throws Exception {
        // Initialize the database
        workOrderVehiculeRepository.saveAndFlush(workOrderVehicule);

        // Get all the workOrderVehiculeList
        restWorkOrderVehiculeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrderVehicule.getId().intValue())))
            .andExpect(jsonPath("$.[*].workOrderId").value(hasItem(DEFAULT_WORK_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].vehiculeId").value(hasItem(DEFAULT_VEHICULE_ID.intValue())));
    }

    @Test
    @Transactional
    void getWorkOrderVehicule() throws Exception {
        // Initialize the database
        workOrderVehiculeRepository.saveAndFlush(workOrderVehicule);

        // Get the workOrderVehicule
        restWorkOrderVehiculeMockMvc
            .perform(get(ENTITY_API_URL_ID, workOrderVehicule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workOrderVehicule.getId().intValue()))
            .andExpect(jsonPath("$.workOrderId").value(DEFAULT_WORK_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.vehiculeId").value(DEFAULT_VEHICULE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingWorkOrderVehicule() throws Exception {
        // Get the workOrderVehicule
        restWorkOrderVehiculeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkOrderVehicule() throws Exception {
        // Initialize the database
        workOrderVehiculeRepository.saveAndFlush(workOrderVehicule);

        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();

        // Update the workOrderVehicule
        WorkOrderVehicule updatedWorkOrderVehicule = workOrderVehiculeRepository.findById(workOrderVehicule.getId()).get();
        // Disconnect from session so that the updates on updatedWorkOrderVehicule are not directly saved in db
        em.detach(updatedWorkOrderVehicule);
        updatedWorkOrderVehicule.workOrderId(UPDATED_WORK_ORDER_ID).vehiculeId(UPDATED_VEHICULE_ID);
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(updatedWorkOrderVehicule);

        restWorkOrderVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrderVehiculeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderVehicule testWorkOrderVehicule = workOrderVehiculeList.get(workOrderVehiculeList.size() - 1);
        assertThat(testWorkOrderVehicule.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
        assertThat(testWorkOrderVehicule.getVehiculeId()).isEqualTo(UPDATED_VEHICULE_ID);
    }

    @Test
    @Transactional
    void putNonExistingWorkOrderVehicule() throws Exception {
        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();
        workOrderVehicule.setId(count.incrementAndGet());

        // Create the WorkOrderVehicule
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(workOrderVehicule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrderVehiculeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkOrderVehicule() throws Exception {
        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();
        workOrderVehicule.setId(count.incrementAndGet());

        // Create the WorkOrderVehicule
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(workOrderVehicule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkOrderVehicule() throws Exception {
        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();
        workOrderVehicule.setId(count.incrementAndGet());

        // Create the WorkOrderVehicule
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(workOrderVehicule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkOrderVehiculeWithPatch() throws Exception {
        // Initialize the database
        workOrderVehiculeRepository.saveAndFlush(workOrderVehicule);

        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();

        // Update the workOrderVehicule using partial update
        WorkOrderVehicule partialUpdatedWorkOrderVehicule = new WorkOrderVehicule();
        partialUpdatedWorkOrderVehicule.setId(workOrderVehicule.getId());

        restWorkOrderVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrderVehicule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrderVehicule))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderVehicule testWorkOrderVehicule = workOrderVehiculeList.get(workOrderVehiculeList.size() - 1);
        assertThat(testWorkOrderVehicule.getWorkOrderId()).isEqualTo(DEFAULT_WORK_ORDER_ID);
        assertThat(testWorkOrderVehicule.getVehiculeId()).isEqualTo(DEFAULT_VEHICULE_ID);
    }

    @Test
    @Transactional
    void fullUpdateWorkOrderVehiculeWithPatch() throws Exception {
        // Initialize the database
        workOrderVehiculeRepository.saveAndFlush(workOrderVehicule);

        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();

        // Update the workOrderVehicule using partial update
        WorkOrderVehicule partialUpdatedWorkOrderVehicule = new WorkOrderVehicule();
        partialUpdatedWorkOrderVehicule.setId(workOrderVehicule.getId());

        partialUpdatedWorkOrderVehicule.workOrderId(UPDATED_WORK_ORDER_ID).vehiculeId(UPDATED_VEHICULE_ID);

        restWorkOrderVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrderVehicule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrderVehicule))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
        WorkOrderVehicule testWorkOrderVehicule = workOrderVehiculeList.get(workOrderVehiculeList.size() - 1);
        assertThat(testWorkOrderVehicule.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
        assertThat(testWorkOrderVehicule.getVehiculeId()).isEqualTo(UPDATED_VEHICULE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingWorkOrderVehicule() throws Exception {
        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();
        workOrderVehicule.setId(count.incrementAndGet());

        // Create the WorkOrderVehicule
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(workOrderVehicule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workOrderVehiculeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkOrderVehicule() throws Exception {
        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();
        workOrderVehicule.setId(count.incrementAndGet());

        // Create the WorkOrderVehicule
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(workOrderVehicule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkOrderVehicule() throws Exception {
        int databaseSizeBeforeUpdate = workOrderVehiculeRepository.findAll().size();
        workOrderVehicule.setId(count.incrementAndGet());

        // Create the WorkOrderVehicule
        WorkOrderVehiculeDTO workOrderVehiculeDTO = workOrderVehiculeMapper.toDto(workOrderVehicule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderVehiculeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrderVehicule in the database
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkOrderVehicule() throws Exception {
        // Initialize the database
        workOrderVehiculeRepository.saveAndFlush(workOrderVehicule);

        int databaseSizeBeforeDelete = workOrderVehiculeRepository.findAll().size();

        // Delete the workOrderVehicule
        restWorkOrderVehiculeMockMvc
            .perform(delete(ENTITY_API_URL_ID, workOrderVehicule.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkOrderVehicule> workOrderVehiculeList = workOrderVehiculeRepository.findAll();
        assertThat(workOrderVehiculeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
