package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.Tache;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.repository.TacheRepository;
import com.gpm.operations.service.TacheService;
import com.gpm.operations.service.dto.TacheDTO;
import com.gpm.operations.service.mapper.TacheMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TacheResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TacheResourceIT {

    private static final String DEFAULT_ROLE_DANS_LA_MISSION = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_DANS_LA_MISSION = "BBBBBBBBBB";

    private static final Float DEFAULT_NOTE = 1F;
    private static final Float UPDATED_NOTE = 2F;

    private static final Float DEFAULT_REMBOURSEMENT = 1F;
    private static final Float UPDATED_REMBOURSEMENT = 2F;

    private static final String DEFAULT_EXECUTEUR_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXECUTEUR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXECUTEUR_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_EXECUTEUR_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/taches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TacheRepository tacheRepository;

    @Mock
    private TacheRepository tacheRepositoryMock;

    @Autowired
    private TacheMapper tacheMapper;

    @Mock
    private TacheService tacheServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTacheMockMvc;

    private Tache tache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tache createEntity(EntityManager em) {
        Tache tache = new Tache()
            .roleDansLaMission(DEFAULT_ROLE_DANS_LA_MISSION)
            .note(DEFAULT_NOTE)
            .remboursement(DEFAULT_REMBOURSEMENT)
            .executeurId(DEFAULT_EXECUTEUR_ID)
            .executeurUserLogin(DEFAULT_EXECUTEUR_USER_LOGIN);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        tache.setWorkOrder(workOrder);
        return tache;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tache createUpdatedEntity(EntityManager em) {
        Tache tache = new Tache()
            .roleDansLaMission(UPDATED_ROLE_DANS_LA_MISSION)
            .note(UPDATED_NOTE)
            .remboursement(UPDATED_REMBOURSEMENT)
            .executeurId(UPDATED_EXECUTEUR_ID)
            .executeurUserLogin(UPDATED_EXECUTEUR_USER_LOGIN);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createUpdatedEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        tache.setWorkOrder(workOrder);
        return tache;
    }

    @BeforeEach
    public void initTest() {
        tache = createEntity(em);
    }

    @Test
    @Transactional
    void createTache() throws Exception {
        int databaseSizeBeforeCreate = tacheRepository.findAll().size();
        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);
        restTacheMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeCreate + 1);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getRoleDansLaMission()).isEqualTo(DEFAULT_ROLE_DANS_LA_MISSION);
        assertThat(testTache.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testTache.getRemboursement()).isEqualTo(DEFAULT_REMBOURSEMENT);
        assertThat(testTache.getExecuteurId()).isEqualTo(DEFAULT_EXECUTEUR_ID);
        assertThat(testTache.getExecuteurUserLogin()).isEqualTo(DEFAULT_EXECUTEUR_USER_LOGIN);
    }

    @Test
    @Transactional
    void createTacheWithExistingId() throws Exception {
        // Create the Tache with an existing ID
        tache.setId(1L);
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        int databaseSizeBeforeCreate = tacheRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTacheMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleDansLaMissionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tacheRepository.findAll().size();
        // set the field null
        tache.setRoleDansLaMission(null);

        // Create the Tache, which fails.
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        restTacheMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaches() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get all the tacheList
        restTacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tache.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleDansLaMission").value(hasItem(DEFAULT_ROLE_DANS_LA_MISSION)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].remboursement").value(hasItem(DEFAULT_REMBOURSEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].executeurId").value(hasItem(DEFAULT_EXECUTEUR_ID)))
            .andExpect(jsonPath("$.[*].executeurUserLogin").value(hasItem(DEFAULT_EXECUTEUR_USER_LOGIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTachesWithEagerRelationshipsIsEnabled() throws Exception {
        when(tacheServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTacheMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tacheServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTachesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tacheServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTacheMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tacheRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get the tache
        restTacheMockMvc
            .perform(get(ENTITY_API_URL_ID, tache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tache.getId().intValue()))
            .andExpect(jsonPath("$.roleDansLaMission").value(DEFAULT_ROLE_DANS_LA_MISSION))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()))
            .andExpect(jsonPath("$.remboursement").value(DEFAULT_REMBOURSEMENT.doubleValue()))
            .andExpect(jsonPath("$.executeurId").value(DEFAULT_EXECUTEUR_ID))
            .andExpect(jsonPath("$.executeurUserLogin").value(DEFAULT_EXECUTEUR_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingTache() throws Exception {
        // Get the tache
        restTacheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache
        Tache updatedTache = tacheRepository.findById(tache.getId()).get();
        // Disconnect from session so that the updates on updatedTache are not directly saved in db
        em.detach(updatedTache);
        updatedTache
            .roleDansLaMission(UPDATED_ROLE_DANS_LA_MISSION)
            .note(UPDATED_NOTE)
            .remboursement(UPDATED_REMBOURSEMENT)
            .executeurId(UPDATED_EXECUTEUR_ID)
            .executeurUserLogin(UPDATED_EXECUTEUR_USER_LOGIN);
        TacheDTO tacheDTO = tacheMapper.toDto(updatedTache);

        restTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tacheDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getRoleDansLaMission()).isEqualTo(UPDATED_ROLE_DANS_LA_MISSION);
        assertThat(testTache.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testTache.getRemboursement()).isEqualTo(UPDATED_REMBOURSEMENT);
        assertThat(testTache.getExecuteurId()).isEqualTo(UPDATED_EXECUTEUR_ID);
        assertThat(testTache.getExecuteurUserLogin()).isEqualTo(UPDATED_EXECUTEUR_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tacheDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTacheWithPatch() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache using partial update
        Tache partialUpdatedTache = new Tache();
        partialUpdatedTache.setId(tache.getId());

        partialUpdatedTache.roleDansLaMission(UPDATED_ROLE_DANS_LA_MISSION).note(UPDATED_NOTE);

        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTache.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTache))
            )
            .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getRoleDansLaMission()).isEqualTo(UPDATED_ROLE_DANS_LA_MISSION);
        assertThat(testTache.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testTache.getRemboursement()).isEqualTo(DEFAULT_REMBOURSEMENT);
        assertThat(testTache.getExecuteurId()).isEqualTo(DEFAULT_EXECUTEUR_ID);
        assertThat(testTache.getExecuteurUserLogin()).isEqualTo(DEFAULT_EXECUTEUR_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateTacheWithPatch() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache using partial update
        Tache partialUpdatedTache = new Tache();
        partialUpdatedTache.setId(tache.getId());

        partialUpdatedTache
            .roleDansLaMission(UPDATED_ROLE_DANS_LA_MISSION)
            .note(UPDATED_NOTE)
            .remboursement(UPDATED_REMBOURSEMENT)
            .executeurId(UPDATED_EXECUTEUR_ID)
            .executeurUserLogin(UPDATED_EXECUTEUR_USER_LOGIN);

        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTache.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTache))
            )
            .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getRoleDansLaMission()).isEqualTo(UPDATED_ROLE_DANS_LA_MISSION);
        assertThat(testTache.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testTache.getRemboursement()).isEqualTo(UPDATED_REMBOURSEMENT);
        assertThat(testTache.getExecuteurId()).isEqualTo(UPDATED_EXECUTEUR_ID);
        assertThat(testTache.getExecuteurUserLogin()).isEqualTo(UPDATED_EXECUTEUR_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tacheDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        int databaseSizeBeforeDelete = tacheRepository.findAll().size();

        // Delete the tache
        restTacheMockMvc
            .perform(delete(ENTITY_API_URL_ID, tache.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
