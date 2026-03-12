package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.WoUtilisateur;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.repository.WoUtilisateurRepository;
import com.gpm.operations.service.dto.WoUtilisateurDTO;
import com.gpm.operations.service.mapper.WoUtilisateurMapper;
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
 * Integration tests for the {@link WoUtilisateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WoUtilisateurResourceIT {

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wo-utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WoUtilisateurRepository woUtilisateurRepository;

    @Autowired
    private WoUtilisateurMapper woUtilisateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWoUtilisateurMockMvc;

    private WoUtilisateur woUtilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WoUtilisateur createEntity(EntityManager em) {
        WoUtilisateur woUtilisateur = new WoUtilisateur().role(DEFAULT_ROLE).userId(DEFAULT_USER_ID).userLogin(DEFAULT_USER_LOGIN);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        woUtilisateur.setWorkOrder(workOrder);
        return woUtilisateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WoUtilisateur createUpdatedEntity(EntityManager em) {
        WoUtilisateur woUtilisateur = new WoUtilisateur().role(UPDATED_ROLE).userId(UPDATED_USER_ID).userLogin(UPDATED_USER_LOGIN);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createUpdatedEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        woUtilisateur.setWorkOrder(workOrder);
        return woUtilisateur;
    }

    @BeforeEach
    public void initTest() {
        woUtilisateur = createEntity(em);
    }

    @Test
    @Transactional
    void createWoUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = woUtilisateurRepository.findAll().size();
        // Create the WoUtilisateur
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);
        restWoUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeCreate + 1);
        WoUtilisateur testWoUtilisateur = woUtilisateurList.get(woUtilisateurList.size() - 1);
        assertThat(testWoUtilisateur.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testWoUtilisateur.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testWoUtilisateur.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void createWoUtilisateurWithExistingId() throws Exception {
        // Create the WoUtilisateur with an existing ID
        woUtilisateur.setId(1L);
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);

        int databaseSizeBeforeCreate = woUtilisateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWoUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = woUtilisateurRepository.findAll().size();
        // set the field null
        woUtilisateur.setRole(null);

        // Create the WoUtilisateur, which fails.
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);

        restWoUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWoUtilisateurs() throws Exception {
        // Initialize the database
        woUtilisateurRepository.saveAndFlush(woUtilisateur);

        // Get all the woUtilisateurList
        restWoUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(woUtilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getWoUtilisateur() throws Exception {
        // Initialize the database
        woUtilisateurRepository.saveAndFlush(woUtilisateur);

        // Get the woUtilisateur
        restWoUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, woUtilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(woUtilisateur.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingWoUtilisateur() throws Exception {
        // Get the woUtilisateur
        restWoUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWoUtilisateur() throws Exception {
        // Initialize the database
        woUtilisateurRepository.saveAndFlush(woUtilisateur);

        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();

        // Update the woUtilisateur
        WoUtilisateur updatedWoUtilisateur = woUtilisateurRepository.findById(woUtilisateur.getId()).get();
        // Disconnect from session so that the updates on updatedWoUtilisateur are not directly saved in db
        em.detach(updatedWoUtilisateur);
        updatedWoUtilisateur.role(UPDATED_ROLE).userId(UPDATED_USER_ID).userLogin(UPDATED_USER_LOGIN);
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(updatedWoUtilisateur);

        restWoUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, woUtilisateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
        WoUtilisateur testWoUtilisateur = woUtilisateurList.get(woUtilisateurList.size() - 1);
        assertThat(testWoUtilisateur.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testWoUtilisateur.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testWoUtilisateur.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingWoUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();
        woUtilisateur.setId(count.incrementAndGet());

        // Create the WoUtilisateur
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWoUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, woUtilisateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWoUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();
        woUtilisateur.setId(count.incrementAndGet());

        // Create the WoUtilisateur
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWoUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();
        woUtilisateur.setId(count.incrementAndGet());

        // Create the WoUtilisateur
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWoUtilisateurWithPatch() throws Exception {
        // Initialize the database
        woUtilisateurRepository.saveAndFlush(woUtilisateur);

        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();

        // Update the woUtilisateur using partial update
        WoUtilisateur partialUpdatedWoUtilisateur = new WoUtilisateur();
        partialUpdatedWoUtilisateur.setId(woUtilisateur.getId());

        partialUpdatedWoUtilisateur.role(UPDATED_ROLE).userId(UPDATED_USER_ID);

        restWoUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWoUtilisateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWoUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
        WoUtilisateur testWoUtilisateur = woUtilisateurList.get(woUtilisateurList.size() - 1);
        assertThat(testWoUtilisateur.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testWoUtilisateur.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testWoUtilisateur.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateWoUtilisateurWithPatch() throws Exception {
        // Initialize the database
        woUtilisateurRepository.saveAndFlush(woUtilisateur);

        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();

        // Update the woUtilisateur using partial update
        WoUtilisateur partialUpdatedWoUtilisateur = new WoUtilisateur();
        partialUpdatedWoUtilisateur.setId(woUtilisateur.getId());

        partialUpdatedWoUtilisateur.role(UPDATED_ROLE).userId(UPDATED_USER_ID).userLogin(UPDATED_USER_LOGIN);

        restWoUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWoUtilisateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWoUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
        WoUtilisateur testWoUtilisateur = woUtilisateurList.get(woUtilisateurList.size() - 1);
        assertThat(testWoUtilisateur.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testWoUtilisateur.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testWoUtilisateur.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingWoUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();
        woUtilisateur.setId(count.incrementAndGet());

        // Create the WoUtilisateur
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWoUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, woUtilisateurDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWoUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();
        woUtilisateur.setId(count.incrementAndGet());

        // Create the WoUtilisateur
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWoUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = woUtilisateurRepository.findAll().size();
        woUtilisateur.setId(count.incrementAndGet());

        // Create the WoUtilisateur
        WoUtilisateurDTO woUtilisateurDTO = woUtilisateurMapper.toDto(woUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woUtilisateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WoUtilisateur in the database
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWoUtilisateur() throws Exception {
        // Initialize the database
        woUtilisateurRepository.saveAndFlush(woUtilisateur);

        int databaseSizeBeforeDelete = woUtilisateurRepository.findAll().size();

        // Delete the woUtilisateur
        restWoUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, woUtilisateur.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WoUtilisateur> woUtilisateurList = woUtilisateurRepository.findAll();
        assertThat(woUtilisateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
