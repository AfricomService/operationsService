package com.gpm.operations.web.rest;

import static com.gpm.operations.web.rest.TestUtil.sameInstant;
import static com.gpm.operations.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.MissionArticles;
import com.gpm.operations.repository.MissionArticlesRepository;
import com.gpm.operations.service.dto.MissionArticlesDTO;
import com.gpm.operations.service.mapper.MissionArticlesMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link MissionArticlesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MissionArticlesResourceIT {

    private static final Long DEFAULT_WO_ID = 1L;
    private static final Long UPDATED_WO_ID = 2L;

    private static final Long DEFAULT_ARTICLE_ID = 1L;
    private static final Long UPDATED_ARTICLE_ID = 2L;

    private static final BigDecimal DEFAULT_PRIX_PROPOSE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_PROPOSE = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_DATE_AFFECTATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_AFFECTATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_QTE = 1;
    private static final Integer UPDATED_QTE = 2;

    private static final Integer DEFAULT_QUANTITE_PLANIFIEE = 1;
    private static final Integer UPDATED_QUANTITE_PLANIFIEE = 2;

    private static final Integer DEFAULT_QUANTITE_REALISEE = 1;
    private static final Integer UPDATED_QUANTITE_REALISEE = 2;

    private static final String ENTITY_API_URL = "/api/mission-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MissionArticlesRepository missionArticlesRepository;

    @Autowired
    private MissionArticlesMapper missionArticlesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMissionArticlesMockMvc;

    private MissionArticles missionArticles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MissionArticles createEntity(EntityManager em) {
        MissionArticles missionArticles = new MissionArticles()
            .woId(DEFAULT_WO_ID)
            .articleId(DEFAULT_ARTICLE_ID)
            .prixPropose(DEFAULT_PRIX_PROPOSE)
            .dateAffectation(DEFAULT_DATE_AFFECTATION)
            .qte(DEFAULT_QTE)
            .quantitePlanifiee(DEFAULT_QUANTITE_PLANIFIEE)
            .quantiteRealisee(DEFAULT_QUANTITE_REALISEE);
        return missionArticles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MissionArticles createUpdatedEntity(EntityManager em) {
        MissionArticles missionArticles = new MissionArticles()
            .woId(UPDATED_WO_ID)
            .articleId(UPDATED_ARTICLE_ID)
            .prixPropose(UPDATED_PRIX_PROPOSE)
            .dateAffectation(UPDATED_DATE_AFFECTATION)
            .qte(UPDATED_QTE)
            .quantitePlanifiee(UPDATED_QUANTITE_PLANIFIEE)
            .quantiteRealisee(UPDATED_QUANTITE_REALISEE);
        return missionArticles;
    }

    @BeforeEach
    public void initTest() {
        missionArticles = createEntity(em);
    }

    @Test
    @Transactional
    void createMissionArticles() throws Exception {
        int databaseSizeBeforeCreate = missionArticlesRepository.findAll().size();
        // Create the MissionArticles
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(missionArticles);
        restMissionArticlesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeCreate + 1);
        MissionArticles testMissionArticles = missionArticlesList.get(missionArticlesList.size() - 1);
        assertThat(testMissionArticles.getWoId()).isEqualTo(DEFAULT_WO_ID);
        assertThat(testMissionArticles.getArticleId()).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(testMissionArticles.getPrixPropose()).isEqualByComparingTo(DEFAULT_PRIX_PROPOSE);
        assertThat(testMissionArticles.getDateAffectation()).isEqualTo(DEFAULT_DATE_AFFECTATION);
        assertThat(testMissionArticles.getQte()).isEqualTo(DEFAULT_QTE);
        assertThat(testMissionArticles.getQuantitePlanifiee()).isEqualTo(DEFAULT_QUANTITE_PLANIFIEE);
        assertThat(testMissionArticles.getQuantiteRealisee()).isEqualTo(DEFAULT_QUANTITE_REALISEE);
    }

    @Test
    @Transactional
    void createMissionArticlesWithExistingId() throws Exception {
        // Create the MissionArticles with an existing ID
        missionArticles.setId(1L);
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(missionArticles);

        int databaseSizeBeforeCreate = missionArticlesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMissionArticlesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMissionArticles() throws Exception {
        // Initialize the database
        missionArticlesRepository.saveAndFlush(missionArticles);

        // Get all the missionArticlesList
        restMissionArticlesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(missionArticles.getId().intValue())))
            .andExpect(jsonPath("$.[*].woId").value(hasItem(DEFAULT_WO_ID.intValue())))
            .andExpect(jsonPath("$.[*].articleId").value(hasItem(DEFAULT_ARTICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].prixPropose").value(hasItem(sameNumber(DEFAULT_PRIX_PROPOSE))))
            .andExpect(jsonPath("$.[*].dateAffectation").value(hasItem(sameInstant(DEFAULT_DATE_AFFECTATION))))
            .andExpect(jsonPath("$.[*].qte").value(hasItem(DEFAULT_QTE)))
            .andExpect(jsonPath("$.[*].quantitePlanifiee").value(hasItem(DEFAULT_QUANTITE_PLANIFIEE)))
            .andExpect(jsonPath("$.[*].quantiteRealisee").value(hasItem(DEFAULT_QUANTITE_REALISEE)));
    }

    @Test
    @Transactional
    void getMissionArticles() throws Exception {
        // Initialize the database
        missionArticlesRepository.saveAndFlush(missionArticles);

        // Get the missionArticles
        restMissionArticlesMockMvc
            .perform(get(ENTITY_API_URL_ID, missionArticles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(missionArticles.getId().intValue()))
            .andExpect(jsonPath("$.woId").value(DEFAULT_WO_ID.intValue()))
            .andExpect(jsonPath("$.articleId").value(DEFAULT_ARTICLE_ID.intValue()))
            .andExpect(jsonPath("$.prixPropose").value(sameNumber(DEFAULT_PRIX_PROPOSE)))
            .andExpect(jsonPath("$.dateAffectation").value(sameInstant(DEFAULT_DATE_AFFECTATION)))
            .andExpect(jsonPath("$.qte").value(DEFAULT_QTE))
            .andExpect(jsonPath("$.quantitePlanifiee").value(DEFAULT_QUANTITE_PLANIFIEE))
            .andExpect(jsonPath("$.quantiteRealisee").value(DEFAULT_QUANTITE_REALISEE));
    }

    @Test
    @Transactional
    void getNonExistingMissionArticles() throws Exception {
        // Get the missionArticles
        restMissionArticlesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMissionArticles() throws Exception {
        // Initialize the database
        missionArticlesRepository.saveAndFlush(missionArticles);

        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();

        // Update the missionArticles
        MissionArticles updatedMissionArticles = missionArticlesRepository.findById(missionArticles.getId()).get();
        // Disconnect from session so that the updates on updatedMissionArticles are not directly saved in db
        em.detach(updatedMissionArticles);
        updatedMissionArticles
            .woId(UPDATED_WO_ID)
            .articleId(UPDATED_ARTICLE_ID)
            .prixPropose(UPDATED_PRIX_PROPOSE)
            .dateAffectation(UPDATED_DATE_AFFECTATION)
            .qte(UPDATED_QTE)
            .quantitePlanifiee(UPDATED_QUANTITE_PLANIFIEE)
            .quantiteRealisee(UPDATED_QUANTITE_REALISEE);
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(updatedMissionArticles);

        restMissionArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, missionArticlesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isOk());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
        MissionArticles testMissionArticles = missionArticlesList.get(missionArticlesList.size() - 1);
        assertThat(testMissionArticles.getWoId()).isEqualTo(UPDATED_WO_ID);
        assertThat(testMissionArticles.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testMissionArticles.getPrixPropose()).isEqualByComparingTo(UPDATED_PRIX_PROPOSE);
        assertThat(testMissionArticles.getDateAffectation()).isEqualTo(UPDATED_DATE_AFFECTATION);
        assertThat(testMissionArticles.getQte()).isEqualTo(UPDATED_QTE);
        assertThat(testMissionArticles.getQuantitePlanifiee()).isEqualTo(UPDATED_QUANTITE_PLANIFIEE);
        assertThat(testMissionArticles.getQuantiteRealisee()).isEqualTo(UPDATED_QUANTITE_REALISEE);
    }

    @Test
    @Transactional
    void putNonExistingMissionArticles() throws Exception {
        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();
        missionArticles.setId(count.incrementAndGet());

        // Create the MissionArticles
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(missionArticles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMissionArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, missionArticlesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMissionArticles() throws Exception {
        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();
        missionArticles.setId(count.incrementAndGet());

        // Create the MissionArticles
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(missionArticles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMissionArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMissionArticles() throws Exception {
        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();
        missionArticles.setId(count.incrementAndGet());

        // Create the MissionArticles
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(missionArticles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMissionArticlesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMissionArticlesWithPatch() throws Exception {
        // Initialize the database
        missionArticlesRepository.saveAndFlush(missionArticles);

        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();

        // Update the missionArticles using partial update
        MissionArticles partialUpdatedMissionArticles = new MissionArticles();
        partialUpdatedMissionArticles.setId(missionArticles.getId());

        partialUpdatedMissionArticles
            .woId(UPDATED_WO_ID)
            .articleId(UPDATED_ARTICLE_ID)
            .dateAffectation(UPDATED_DATE_AFFECTATION)
            .quantitePlanifiee(UPDATED_QUANTITE_PLANIFIEE)
            .quantiteRealisee(UPDATED_QUANTITE_REALISEE);

        restMissionArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMissionArticles.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMissionArticles))
            )
            .andExpect(status().isOk());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
        MissionArticles testMissionArticles = missionArticlesList.get(missionArticlesList.size() - 1);
        assertThat(testMissionArticles.getWoId()).isEqualTo(UPDATED_WO_ID);
        assertThat(testMissionArticles.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testMissionArticles.getPrixPropose()).isEqualByComparingTo(DEFAULT_PRIX_PROPOSE);
        assertThat(testMissionArticles.getDateAffectation()).isEqualTo(UPDATED_DATE_AFFECTATION);
        assertThat(testMissionArticles.getQte()).isEqualTo(DEFAULT_QTE);
        assertThat(testMissionArticles.getQuantitePlanifiee()).isEqualTo(UPDATED_QUANTITE_PLANIFIEE);
        assertThat(testMissionArticles.getQuantiteRealisee()).isEqualTo(UPDATED_QUANTITE_REALISEE);
    }

    @Test
    @Transactional
    void fullUpdateMissionArticlesWithPatch() throws Exception {
        // Initialize the database
        missionArticlesRepository.saveAndFlush(missionArticles);

        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();

        // Update the missionArticles using partial update
        MissionArticles partialUpdatedMissionArticles = new MissionArticles();
        partialUpdatedMissionArticles.setId(missionArticles.getId());

        partialUpdatedMissionArticles
            .woId(UPDATED_WO_ID)
            .articleId(UPDATED_ARTICLE_ID)
            .prixPropose(UPDATED_PRIX_PROPOSE)
            .dateAffectation(UPDATED_DATE_AFFECTATION)
            .qte(UPDATED_QTE)
            .quantitePlanifiee(UPDATED_QUANTITE_PLANIFIEE)
            .quantiteRealisee(UPDATED_QUANTITE_REALISEE);

        restMissionArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMissionArticles.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMissionArticles))
            )
            .andExpect(status().isOk());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
        MissionArticles testMissionArticles = missionArticlesList.get(missionArticlesList.size() - 1);
        assertThat(testMissionArticles.getWoId()).isEqualTo(UPDATED_WO_ID);
        assertThat(testMissionArticles.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testMissionArticles.getPrixPropose()).isEqualByComparingTo(UPDATED_PRIX_PROPOSE);
        assertThat(testMissionArticles.getDateAffectation()).isEqualTo(UPDATED_DATE_AFFECTATION);
        assertThat(testMissionArticles.getQte()).isEqualTo(UPDATED_QTE);
        assertThat(testMissionArticles.getQuantitePlanifiee()).isEqualTo(UPDATED_QUANTITE_PLANIFIEE);
        assertThat(testMissionArticles.getQuantiteRealisee()).isEqualTo(UPDATED_QUANTITE_REALISEE);
    }

    @Test
    @Transactional
    void patchNonExistingMissionArticles() throws Exception {
        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();
        missionArticles.setId(count.incrementAndGet());

        // Create the MissionArticles
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(missionArticles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMissionArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, missionArticlesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMissionArticles() throws Exception {
        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();
        missionArticles.setId(count.incrementAndGet());

        // Create the MissionArticles
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(missionArticles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMissionArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMissionArticles() throws Exception {
        int databaseSizeBeforeUpdate = missionArticlesRepository.findAll().size();
        missionArticles.setId(count.incrementAndGet());

        // Create the MissionArticles
        MissionArticlesDTO missionArticlesDTO = missionArticlesMapper.toDto(missionArticles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMissionArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(missionArticlesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MissionArticles in the database
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMissionArticles() throws Exception {
        // Initialize the database
        missionArticlesRepository.saveAndFlush(missionArticles);

        int databaseSizeBeforeDelete = missionArticlesRepository.findAll().size();

        // Delete the missionArticles
        restMissionArticlesMockMvc
            .perform(delete(ENTITY_API_URL_ID, missionArticles.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MissionArticles> missionArticlesList = missionArticlesRepository.findAll();
        assertThat(missionArticlesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
