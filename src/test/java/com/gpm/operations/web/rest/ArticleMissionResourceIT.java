package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.ArticleMission;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.repository.ArticleMissionRepository;
import com.gpm.operations.service.dto.ArticleMissionDTO;
import com.gpm.operations.service.mapper.ArticleMissionMapper;
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
 * Integration tests for the {@link ArticleMissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleMissionResourceIT {

    private static final Integer DEFAULT_ITEM = 1;
    private static final Integer UPDATED_ITEM = 2;

    private static final Float DEFAULT_QUANTITE_PLANIFIEE = 1F;
    private static final Float UPDATED_QUANTITE_PLANIFIEE = 2F;

    private static final Float DEFAULT_QUANTITE_REALISEE = 1F;
    private static final Float UPDATED_QUANTITE_REALISEE = 2F;

    private static final String DEFAULT_ARTICLE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/article-missions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArticleMissionRepository articleMissionRepository;

    @Autowired
    private ArticleMissionMapper articleMissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMissionMockMvc;

    private ArticleMission articleMission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleMission createEntity(EntityManager em) {
        ArticleMission articleMission = new ArticleMission()
            .item(DEFAULT_ITEM)
            .quantitePlanifiee(DEFAULT_QUANTITE_PLANIFIEE)
            .quantiteRealisee(DEFAULT_QUANTITE_REALISEE)
            .articleCode(DEFAULT_ARTICLE_CODE);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        articleMission.setWorkOrder(workOrder);
        return articleMission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleMission createUpdatedEntity(EntityManager em) {
        ArticleMission articleMission = new ArticleMission()
            .item(UPDATED_ITEM)
            .quantitePlanifiee(UPDATED_QUANTITE_PLANIFIEE)
            .quantiteRealisee(UPDATED_QUANTITE_REALISEE)
            .articleCode(UPDATED_ARTICLE_CODE);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createUpdatedEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        articleMission.setWorkOrder(workOrder);
        return articleMission;
    }

    @BeforeEach
    public void initTest() {
        articleMission = createEntity(em);
    }

    @Test
    @Transactional
    void createArticleMission() throws Exception {
        int databaseSizeBeforeCreate = articleMissionRepository.findAll().size();
        // Create the ArticleMission
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);
        restArticleMissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeCreate + 1);
        ArticleMission testArticleMission = articleMissionList.get(articleMissionList.size() - 1);
        assertThat(testArticleMission.getItem()).isEqualTo(DEFAULT_ITEM);
        assertThat(testArticleMission.getQuantitePlanifiee()).isEqualTo(DEFAULT_QUANTITE_PLANIFIEE);
        assertThat(testArticleMission.getQuantiteRealisee()).isEqualTo(DEFAULT_QUANTITE_REALISEE);
        assertThat(testArticleMission.getArticleCode()).isEqualTo(DEFAULT_ARTICLE_CODE);
    }

    @Test
    @Transactional
    void createArticleMissionWithExistingId() throws Exception {
        // Create the ArticleMission with an existing ID
        articleMission.setId(1L);
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        int databaseSizeBeforeCreate = articleMissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkItemIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleMissionRepository.findAll().size();
        // set the field null
        articleMission.setItem(null);

        // Create the ArticleMission, which fails.
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        restArticleMissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantitePlanifieeIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleMissionRepository.findAll().size();
        // set the field null
        articleMission.setQuantitePlanifiee(null);

        // Create the ArticleMission, which fails.
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        restArticleMissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticleMissions() throws Exception {
        // Initialize the database
        articleMissionRepository.saveAndFlush(articleMission);

        // Get all the articleMissionList
        restArticleMissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleMission.getId().intValue())))
            .andExpect(jsonPath("$.[*].item").value(hasItem(DEFAULT_ITEM)))
            .andExpect(jsonPath("$.[*].quantitePlanifiee").value(hasItem(DEFAULT_QUANTITE_PLANIFIEE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantiteRealisee").value(hasItem(DEFAULT_QUANTITE_REALISEE.doubleValue())))
            .andExpect(jsonPath("$.[*].articleCode").value(hasItem(DEFAULT_ARTICLE_CODE)));
    }

    @Test
    @Transactional
    void getArticleMission() throws Exception {
        // Initialize the database
        articleMissionRepository.saveAndFlush(articleMission);

        // Get the articleMission
        restArticleMissionMockMvc
            .perform(get(ENTITY_API_URL_ID, articleMission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleMission.getId().intValue()))
            .andExpect(jsonPath("$.item").value(DEFAULT_ITEM))
            .andExpect(jsonPath("$.quantitePlanifiee").value(DEFAULT_QUANTITE_PLANIFIEE.doubleValue()))
            .andExpect(jsonPath("$.quantiteRealisee").value(DEFAULT_QUANTITE_REALISEE.doubleValue()))
            .andExpect(jsonPath("$.articleCode").value(DEFAULT_ARTICLE_CODE));
    }

    @Test
    @Transactional
    void getNonExistingArticleMission() throws Exception {
        // Get the articleMission
        restArticleMissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticleMission() throws Exception {
        // Initialize the database
        articleMissionRepository.saveAndFlush(articleMission);

        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();

        // Update the articleMission
        ArticleMission updatedArticleMission = articleMissionRepository.findById(articleMission.getId()).get();
        // Disconnect from session so that the updates on updatedArticleMission are not directly saved in db
        em.detach(updatedArticleMission);
        updatedArticleMission
            .item(UPDATED_ITEM)
            .quantitePlanifiee(UPDATED_QUANTITE_PLANIFIEE)
            .quantiteRealisee(UPDATED_QUANTITE_REALISEE)
            .articleCode(UPDATED_ARTICLE_CODE);
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(updatedArticleMission);

        restArticleMissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleMissionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
        ArticleMission testArticleMission = articleMissionList.get(articleMissionList.size() - 1);
        assertThat(testArticleMission.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testArticleMission.getQuantitePlanifiee()).isEqualTo(UPDATED_QUANTITE_PLANIFIEE);
        assertThat(testArticleMission.getQuantiteRealisee()).isEqualTo(UPDATED_QUANTITE_REALISEE);
        assertThat(testArticleMission.getArticleCode()).isEqualTo(UPDATED_ARTICLE_CODE);
    }

    @Test
    @Transactional
    void putNonExistingArticleMission() throws Exception {
        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();
        articleMission.setId(count.incrementAndGet());

        // Create the ArticleMission
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleMissionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticleMission() throws Exception {
        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();
        articleMission.setId(count.incrementAndGet());

        // Create the ArticleMission
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticleMission() throws Exception {
        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();
        articleMission.setId(count.incrementAndGet());

        // Create the ArticleMission
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMissionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleMissionWithPatch() throws Exception {
        // Initialize the database
        articleMissionRepository.saveAndFlush(articleMission);

        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();

        // Update the articleMission using partial update
        ArticleMission partialUpdatedArticleMission = new ArticleMission();
        partialUpdatedArticleMission.setId(articleMission.getId());

        partialUpdatedArticleMission.quantitePlanifiee(UPDATED_QUANTITE_PLANIFIEE).articleCode(UPDATED_ARTICLE_CODE);

        restArticleMissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleMission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticleMission))
            )
            .andExpect(status().isOk());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
        ArticleMission testArticleMission = articleMissionList.get(articleMissionList.size() - 1);
        assertThat(testArticleMission.getItem()).isEqualTo(DEFAULT_ITEM);
        assertThat(testArticleMission.getQuantitePlanifiee()).isEqualTo(UPDATED_QUANTITE_PLANIFIEE);
        assertThat(testArticleMission.getQuantiteRealisee()).isEqualTo(DEFAULT_QUANTITE_REALISEE);
        assertThat(testArticleMission.getArticleCode()).isEqualTo(UPDATED_ARTICLE_CODE);
    }

    @Test
    @Transactional
    void fullUpdateArticleMissionWithPatch() throws Exception {
        // Initialize the database
        articleMissionRepository.saveAndFlush(articleMission);

        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();

        // Update the articleMission using partial update
        ArticleMission partialUpdatedArticleMission = new ArticleMission();
        partialUpdatedArticleMission.setId(articleMission.getId());

        partialUpdatedArticleMission
            .item(UPDATED_ITEM)
            .quantitePlanifiee(UPDATED_QUANTITE_PLANIFIEE)
            .quantiteRealisee(UPDATED_QUANTITE_REALISEE)
            .articleCode(UPDATED_ARTICLE_CODE);

        restArticleMissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleMission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticleMission))
            )
            .andExpect(status().isOk());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
        ArticleMission testArticleMission = articleMissionList.get(articleMissionList.size() - 1);
        assertThat(testArticleMission.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testArticleMission.getQuantitePlanifiee()).isEqualTo(UPDATED_QUANTITE_PLANIFIEE);
        assertThat(testArticleMission.getQuantiteRealisee()).isEqualTo(UPDATED_QUANTITE_REALISEE);
        assertThat(testArticleMission.getArticleCode()).isEqualTo(UPDATED_ARTICLE_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingArticleMission() throws Exception {
        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();
        articleMission.setId(count.incrementAndGet());

        // Create the ArticleMission
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleMissionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticleMission() throws Exception {
        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();
        articleMission.setId(count.incrementAndGet());

        // Create the ArticleMission
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticleMission() throws Exception {
        int databaseSizeBeforeUpdate = articleMissionRepository.findAll().size();
        articleMission.setId(count.incrementAndGet());

        // Create the ArticleMission
        ArticleMissionDTO articleMissionDTO = articleMissionMapper.toDto(articleMission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleMissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleMission in the database
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticleMission() throws Exception {
        // Initialize the database
        articleMissionRepository.saveAndFlush(articleMission);

        int databaseSizeBeforeDelete = articleMissionRepository.findAll().size();

        // Delete the articleMission
        restArticleMissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, articleMission.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArticleMission> articleMissionList = articleMissionRepository.findAll();
        assertThat(articleMissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
