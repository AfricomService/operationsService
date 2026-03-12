package com.gpm.operations.web.rest;

import static com.gpm.operations.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.HistoriqueStatutWO;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.repository.HistoriqueStatutWORepository;
import com.gpm.operations.service.dto.HistoriqueStatutWODTO;
import com.gpm.operations.service.mapper.HistoriqueStatutWOMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link HistoriqueStatutWOResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueStatutWOResourceIT {

    private static final StatutWO DEFAULT_ANCIEN_STATUT = StatutWO.Creation;
    private static final StatutWO UPDATED_ANCIEN_STATUT = StatutWO.ExecutionTravaux;

    private static final StatutWO DEFAULT_NOUVEAU_STATUT = StatutWO.Creation;
    private static final StatutWO UPDATED_NOUVEAU_STATUT = StatutWO.ExecutionTravaux;

    private static final ZonedDateTime DEFAULT_DATE_CHANGEMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CHANGEMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historique-statut-wos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoriqueStatutWORepository historiqueStatutWORepository;

    @Autowired
    private HistoriqueStatutWOMapper historiqueStatutWOMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueStatutWOMockMvc;

    private HistoriqueStatutWO historiqueStatutWO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueStatutWO createEntity(EntityManager em) {
        HistoriqueStatutWO historiqueStatutWO = new HistoriqueStatutWO()
            .ancienStatut(DEFAULT_ANCIEN_STATUT)
            .nouveauStatut(DEFAULT_NOUVEAU_STATUT)
            .dateChangement(DEFAULT_DATE_CHANGEMENT)
            .commentaire(DEFAULT_COMMENTAIRE)
            .userId(DEFAULT_USER_ID)
            .userLogin(DEFAULT_USER_LOGIN);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        historiqueStatutWO.setWorkOrder(workOrder);
        return historiqueStatutWO;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueStatutWO createUpdatedEntity(EntityManager em) {
        HistoriqueStatutWO historiqueStatutWO = new HistoriqueStatutWO()
            .ancienStatut(UPDATED_ANCIEN_STATUT)
            .nouveauStatut(UPDATED_NOUVEAU_STATUT)
            .dateChangement(UPDATED_DATE_CHANGEMENT)
            .commentaire(UPDATED_COMMENTAIRE)
            .userId(UPDATED_USER_ID)
            .userLogin(UPDATED_USER_LOGIN);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createUpdatedEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        historiqueStatutWO.setWorkOrder(workOrder);
        return historiqueStatutWO;
    }

    @BeforeEach
    public void initTest() {
        historiqueStatutWO = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoriqueStatutWO() throws Exception {
        int databaseSizeBeforeCreate = historiqueStatutWORepository.findAll().size();
        // Create the HistoriqueStatutWO
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);
        restHistoriqueStatutWOMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isCreated());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriqueStatutWO testHistoriqueStatutWO = historiqueStatutWOList.get(historiqueStatutWOList.size() - 1);
        assertThat(testHistoriqueStatutWO.getAncienStatut()).isEqualTo(DEFAULT_ANCIEN_STATUT);
        assertThat(testHistoriqueStatutWO.getNouveauStatut()).isEqualTo(DEFAULT_NOUVEAU_STATUT);
        assertThat(testHistoriqueStatutWO.getDateChangement()).isEqualTo(DEFAULT_DATE_CHANGEMENT);
        assertThat(testHistoriqueStatutWO.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testHistoriqueStatutWO.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testHistoriqueStatutWO.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void createHistoriqueStatutWOWithExistingId() throws Exception {
        // Create the HistoriqueStatutWO with an existing ID
        historiqueStatutWO.setId(1L);
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        int databaseSizeBeforeCreate = historiqueStatutWORepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueStatutWOMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAncienStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueStatutWORepository.findAll().size();
        // set the field null
        historiqueStatutWO.setAncienStatut(null);

        // Create the HistoriqueStatutWO, which fails.
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        restHistoriqueStatutWOMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNouveauStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueStatutWORepository.findAll().size();
        // set the field null
        historiqueStatutWO.setNouveauStatut(null);

        // Create the HistoriqueStatutWO, which fails.
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        restHistoriqueStatutWOMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateChangementIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueStatutWORepository.findAll().size();
        // set the field null
        historiqueStatutWO.setDateChangement(null);

        // Create the HistoriqueStatutWO, which fails.
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        restHistoriqueStatutWOMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriqueStatutWOS() throws Exception {
        // Initialize the database
        historiqueStatutWORepository.saveAndFlush(historiqueStatutWO);

        // Get all the historiqueStatutWOList
        restHistoriqueStatutWOMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueStatutWO.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancienStatut").value(hasItem(DEFAULT_ANCIEN_STATUT.toString())))
            .andExpect(jsonPath("$.[*].nouveauStatut").value(hasItem(DEFAULT_NOUVEAU_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateChangement").value(hasItem(sameInstant(DEFAULT_DATE_CHANGEMENT))))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getHistoriqueStatutWO() throws Exception {
        // Initialize the database
        historiqueStatutWORepository.saveAndFlush(historiqueStatutWO);

        // Get the historiqueStatutWO
        restHistoriqueStatutWOMockMvc
            .perform(get(ENTITY_API_URL_ID, historiqueStatutWO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueStatutWO.getId().intValue()))
            .andExpect(jsonPath("$.ancienStatut").value(DEFAULT_ANCIEN_STATUT.toString()))
            .andExpect(jsonPath("$.nouveauStatut").value(DEFAULT_NOUVEAU_STATUT.toString()))
            .andExpect(jsonPath("$.dateChangement").value(sameInstant(DEFAULT_DATE_CHANGEMENT)))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingHistoriqueStatutWO() throws Exception {
        // Get the historiqueStatutWO
        restHistoriqueStatutWOMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriqueStatutWO() throws Exception {
        // Initialize the database
        historiqueStatutWORepository.saveAndFlush(historiqueStatutWO);

        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();

        // Update the historiqueStatutWO
        HistoriqueStatutWO updatedHistoriqueStatutWO = historiqueStatutWORepository.findById(historiqueStatutWO.getId()).get();
        // Disconnect from session so that the updates on updatedHistoriqueStatutWO are not directly saved in db
        em.detach(updatedHistoriqueStatutWO);
        updatedHistoriqueStatutWO
            .ancienStatut(UPDATED_ANCIEN_STATUT)
            .nouveauStatut(UPDATED_NOUVEAU_STATUT)
            .dateChangement(UPDATED_DATE_CHANGEMENT)
            .commentaire(UPDATED_COMMENTAIRE)
            .userId(UPDATED_USER_ID)
            .userLogin(UPDATED_USER_LOGIN);
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(updatedHistoriqueStatutWO);

        restHistoriqueStatutWOMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueStatutWODTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueStatutWO testHistoriqueStatutWO = historiqueStatutWOList.get(historiqueStatutWOList.size() - 1);
        assertThat(testHistoriqueStatutWO.getAncienStatut()).isEqualTo(UPDATED_ANCIEN_STATUT);
        assertThat(testHistoriqueStatutWO.getNouveauStatut()).isEqualTo(UPDATED_NOUVEAU_STATUT);
        assertThat(testHistoriqueStatutWO.getDateChangement()).isEqualTo(UPDATED_DATE_CHANGEMENT);
        assertThat(testHistoriqueStatutWO.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testHistoriqueStatutWO.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testHistoriqueStatutWO.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingHistoriqueStatutWO() throws Exception {
        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();
        historiqueStatutWO.setId(count.incrementAndGet());

        // Create the HistoriqueStatutWO
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueStatutWOMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueStatutWODTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriqueStatutWO() throws Exception {
        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();
        historiqueStatutWO.setId(count.incrementAndGet());

        // Create the HistoriqueStatutWO
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueStatutWOMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriqueStatutWO() throws Exception {
        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();
        historiqueStatutWO.setId(count.incrementAndGet());

        // Create the HistoriqueStatutWO
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueStatutWOMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriqueStatutWOWithPatch() throws Exception {
        // Initialize the database
        historiqueStatutWORepository.saveAndFlush(historiqueStatutWO);

        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();

        // Update the historiqueStatutWO using partial update
        HistoriqueStatutWO partialUpdatedHistoriqueStatutWO = new HistoriqueStatutWO();
        partialUpdatedHistoriqueStatutWO.setId(historiqueStatutWO.getId());

        partialUpdatedHistoriqueStatutWO
            .nouveauStatut(UPDATED_NOUVEAU_STATUT)
            .dateChangement(UPDATED_DATE_CHANGEMENT)
            .commentaire(UPDATED_COMMENTAIRE);

        restHistoriqueStatutWOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueStatutWO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriqueStatutWO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueStatutWO testHistoriqueStatutWO = historiqueStatutWOList.get(historiqueStatutWOList.size() - 1);
        assertThat(testHistoriqueStatutWO.getAncienStatut()).isEqualTo(DEFAULT_ANCIEN_STATUT);
        assertThat(testHistoriqueStatutWO.getNouveauStatut()).isEqualTo(UPDATED_NOUVEAU_STATUT);
        assertThat(testHistoriqueStatutWO.getDateChangement()).isEqualTo(UPDATED_DATE_CHANGEMENT);
        assertThat(testHistoriqueStatutWO.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testHistoriqueStatutWO.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testHistoriqueStatutWO.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateHistoriqueStatutWOWithPatch() throws Exception {
        // Initialize the database
        historiqueStatutWORepository.saveAndFlush(historiqueStatutWO);

        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();

        // Update the historiqueStatutWO using partial update
        HistoriqueStatutWO partialUpdatedHistoriqueStatutWO = new HistoriqueStatutWO();
        partialUpdatedHistoriqueStatutWO.setId(historiqueStatutWO.getId());

        partialUpdatedHistoriqueStatutWO
            .ancienStatut(UPDATED_ANCIEN_STATUT)
            .nouveauStatut(UPDATED_NOUVEAU_STATUT)
            .dateChangement(UPDATED_DATE_CHANGEMENT)
            .commentaire(UPDATED_COMMENTAIRE)
            .userId(UPDATED_USER_ID)
            .userLogin(UPDATED_USER_LOGIN);

        restHistoriqueStatutWOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueStatutWO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriqueStatutWO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueStatutWO testHistoriqueStatutWO = historiqueStatutWOList.get(historiqueStatutWOList.size() - 1);
        assertThat(testHistoriqueStatutWO.getAncienStatut()).isEqualTo(UPDATED_ANCIEN_STATUT);
        assertThat(testHistoriqueStatutWO.getNouveauStatut()).isEqualTo(UPDATED_NOUVEAU_STATUT);
        assertThat(testHistoriqueStatutWO.getDateChangement()).isEqualTo(UPDATED_DATE_CHANGEMENT);
        assertThat(testHistoriqueStatutWO.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testHistoriqueStatutWO.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testHistoriqueStatutWO.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingHistoriqueStatutWO() throws Exception {
        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();
        historiqueStatutWO.setId(count.incrementAndGet());

        // Create the HistoriqueStatutWO
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueStatutWOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiqueStatutWODTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriqueStatutWO() throws Exception {
        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();
        historiqueStatutWO.setId(count.incrementAndGet());

        // Create the HistoriqueStatutWO
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueStatutWOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriqueStatutWO() throws Exception {
        int databaseSizeBeforeUpdate = historiqueStatutWORepository.findAll().size();
        historiqueStatutWO.setId(count.incrementAndGet());

        // Create the HistoriqueStatutWO
        HistoriqueStatutWODTO historiqueStatutWODTO = historiqueStatutWOMapper.toDto(historiqueStatutWO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueStatutWOMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiqueStatutWODTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueStatutWO in the database
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriqueStatutWO() throws Exception {
        // Initialize the database
        historiqueStatutWORepository.saveAndFlush(historiqueStatutWO);

        int databaseSizeBeforeDelete = historiqueStatutWORepository.findAll().size();

        // Delete the historiqueStatutWO
        restHistoriqueStatutWOMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiqueStatutWO.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoriqueStatutWO> historiqueStatutWOList = historiqueStatutWORepository.findAll();
        assertThat(historiqueStatutWOList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
