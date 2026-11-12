package com.gpm.operations.web.rest;

import static com.gpm.operations.web.rest.TestUtil.sameInstant;
import static com.gpm.operations.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.WoArticles;
import com.gpm.operations.repository.WoArticlesRepository;
import com.gpm.operations.service.dto.WoArticlesDTO;
import com.gpm.operations.service.mapper.WoArticlesMapper;
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
 * Integration tests for the {@link WoArticlesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WoArticlesResourceIT {

    private static final Long DEFAULT_WO_ID = 1L;
    private static final Long UPDATED_WO_ID = 2L;

    private static final Long DEFAULT_ARTICLE_ID = 1L;
    private static final Long UPDATED_ARTICLE_ID = 2L;

    private static final ZonedDateTime DEFAULT_DATE_REALISATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_REALISATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_QTE_COMMANDE = 1;
    private static final Integer UPDATED_QTE_COMMANDE = 2;

    private static final Integer DEFAULT_QTE_EFFECTUEE = 1;
    private static final Integer UPDATED_QTE_EFFECTUEE = 2;

    private static final BigDecimal DEFAULT_PRIX_ARTICLE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_ARTICLE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PRIX_ARTICLE_HT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_ARTICLE_HT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/wo-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WoArticlesRepository woArticlesRepository;

    @Autowired
    private WoArticlesMapper woArticlesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWoArticlesMockMvc;

    private WoArticles woArticles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WoArticles createEntity(EntityManager em) {
        WoArticles woArticles = new WoArticles()
            .woId(DEFAULT_WO_ID)
            .articleId(DEFAULT_ARTICLE_ID)
            .dateRealisation(DEFAULT_DATE_REALISATION)
            .qteCommande(DEFAULT_QTE_COMMANDE)
            .qteEffectuee(DEFAULT_QTE_EFFECTUEE)
            .prixArticle(DEFAULT_PRIX_ARTICLE)
            .prixArticleHT(DEFAULT_PRIX_ARTICLE_HT);
        return woArticles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WoArticles createUpdatedEntity(EntityManager em) {
        WoArticles woArticles = new WoArticles()
            .woId(UPDATED_WO_ID)
            .articleId(UPDATED_ARTICLE_ID)
            .dateRealisation(UPDATED_DATE_REALISATION)
            .qteCommande(UPDATED_QTE_COMMANDE)
            .qteEffectuee(UPDATED_QTE_EFFECTUEE)
            .prixArticle(UPDATED_PRIX_ARTICLE)
            .prixArticleHT(UPDATED_PRIX_ARTICLE_HT);
        return woArticles;
    }

    @BeforeEach
    public void initTest() {
        woArticles = createEntity(em);
    }

    @Test
    @Transactional
    void createWoArticles() throws Exception {
        int databaseSizeBeforeCreate = woArticlesRepository.findAll().size();
        // Create the WoArticles
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(woArticles);
        restWoArticlesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeCreate + 1);
        WoArticles testWoArticles = woArticlesList.get(woArticlesList.size() - 1);
        assertThat(testWoArticles.getWoId()).isEqualTo(DEFAULT_WO_ID);
        assertThat(testWoArticles.getArticleId()).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(testWoArticles.getDateRealisation()).isEqualTo(DEFAULT_DATE_REALISATION);
        assertThat(testWoArticles.getQteCommande()).isEqualTo(DEFAULT_QTE_COMMANDE);
        assertThat(testWoArticles.getQteEffectuee()).isEqualTo(DEFAULT_QTE_EFFECTUEE);
        assertThat(testWoArticles.getPrixArticle()).isEqualByComparingTo(DEFAULT_PRIX_ARTICLE);
        assertThat(testWoArticles.getPrixArticleHT()).isEqualByComparingTo(DEFAULT_PRIX_ARTICLE_HT);
    }

    @Test
    @Transactional
    void createWoArticlesWithExistingId() throws Exception {
        // Create the WoArticles with an existing ID
        woArticles.setId(1L);
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(woArticles);

        int databaseSizeBeforeCreate = woArticlesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWoArticlesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWoArticles() throws Exception {
        // Initialize the database
        woArticlesRepository.saveAndFlush(woArticles);

        // Get all the woArticlesList
        restWoArticlesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(woArticles.getId().intValue())))
            .andExpect(jsonPath("$.[*].woId").value(hasItem(DEFAULT_WO_ID.intValue())))
            .andExpect(jsonPath("$.[*].articleId").value(hasItem(DEFAULT_ARTICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dateRealisation").value(hasItem(sameInstant(DEFAULT_DATE_REALISATION))))
            .andExpect(jsonPath("$.[*].qteCommande").value(hasItem(DEFAULT_QTE_COMMANDE)))
            .andExpect(jsonPath("$.[*].qteEffectuee").value(hasItem(DEFAULT_QTE_EFFECTUEE)))
            .andExpect(jsonPath("$.[*].prixArticle").value(hasItem(sameNumber(DEFAULT_PRIX_ARTICLE))))
            .andExpect(jsonPath("$.[*].prixArticleHT").value(hasItem(sameNumber(DEFAULT_PRIX_ARTICLE_HT))));
    }

    @Test
    @Transactional
    void getWoArticles() throws Exception {
        // Initialize the database
        woArticlesRepository.saveAndFlush(woArticles);

        // Get the woArticles
        restWoArticlesMockMvc
            .perform(get(ENTITY_API_URL_ID, woArticles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(woArticles.getId().intValue()))
            .andExpect(jsonPath("$.woId").value(DEFAULT_WO_ID.intValue()))
            .andExpect(jsonPath("$.articleId").value(DEFAULT_ARTICLE_ID.intValue()))
            .andExpect(jsonPath("$.dateRealisation").value(sameInstant(DEFAULT_DATE_REALISATION)))
            .andExpect(jsonPath("$.qteCommande").value(DEFAULT_QTE_COMMANDE))
            .andExpect(jsonPath("$.qteEffectuee").value(DEFAULT_QTE_EFFECTUEE))
            .andExpect(jsonPath("$.prixArticle").value(sameNumber(DEFAULT_PRIX_ARTICLE)))
            .andExpect(jsonPath("$.prixArticleHT").value(sameNumber(DEFAULT_PRIX_ARTICLE_HT)));
    }

    @Test
    @Transactional
    void getNonExistingWoArticles() throws Exception {
        // Get the woArticles
        restWoArticlesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWoArticles() throws Exception {
        // Initialize the database
        woArticlesRepository.saveAndFlush(woArticles);

        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();

        // Update the woArticles
        WoArticles updatedWoArticles = woArticlesRepository.findById(woArticles.getId()).get();
        // Disconnect from session so that the updates on updatedWoArticles are not directly saved in db
        em.detach(updatedWoArticles);
        updatedWoArticles
            .woId(UPDATED_WO_ID)
            .articleId(UPDATED_ARTICLE_ID)
            .dateRealisation(UPDATED_DATE_REALISATION)
            .qteCommande(UPDATED_QTE_COMMANDE)
            .qteEffectuee(UPDATED_QTE_EFFECTUEE)
            .prixArticle(UPDATED_PRIX_ARTICLE)
            .prixArticleHT(UPDATED_PRIX_ARTICLE_HT);
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(updatedWoArticles);

        restWoArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, woArticlesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isOk());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
        WoArticles testWoArticles = woArticlesList.get(woArticlesList.size() - 1);
        assertThat(testWoArticles.getWoId()).isEqualTo(UPDATED_WO_ID);
        assertThat(testWoArticles.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testWoArticles.getDateRealisation()).isEqualTo(UPDATED_DATE_REALISATION);
        assertThat(testWoArticles.getQteCommande()).isEqualTo(UPDATED_QTE_COMMANDE);
        assertThat(testWoArticles.getQteEffectuee()).isEqualTo(UPDATED_QTE_EFFECTUEE);
        assertThat(testWoArticles.getPrixArticle()).isEqualByComparingTo(UPDATED_PRIX_ARTICLE);
        assertThat(testWoArticles.getPrixArticleHT()).isEqualByComparingTo(UPDATED_PRIX_ARTICLE_HT);
    }

    @Test
    @Transactional
    void putNonExistingWoArticles() throws Exception {
        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();
        woArticles.setId(count.incrementAndGet());

        // Create the WoArticles
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(woArticles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWoArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, woArticlesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWoArticles() throws Exception {
        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();
        woArticles.setId(count.incrementAndGet());

        // Create the WoArticles
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(woArticles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWoArticles() throws Exception {
        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();
        woArticles.setId(count.incrementAndGet());

        // Create the WoArticles
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(woArticles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoArticlesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWoArticlesWithPatch() throws Exception {
        // Initialize the database
        woArticlesRepository.saveAndFlush(woArticles);

        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();

        // Update the woArticles using partial update
        WoArticles partialUpdatedWoArticles = new WoArticles();
        partialUpdatedWoArticles.setId(woArticles.getId());

        partialUpdatedWoArticles.dateRealisation(UPDATED_DATE_REALISATION).prixArticle(UPDATED_PRIX_ARTICLE);

        restWoArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWoArticles.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWoArticles))
            )
            .andExpect(status().isOk());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
        WoArticles testWoArticles = woArticlesList.get(woArticlesList.size() - 1);
        assertThat(testWoArticles.getWoId()).isEqualTo(DEFAULT_WO_ID);
        assertThat(testWoArticles.getArticleId()).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(testWoArticles.getDateRealisation()).isEqualTo(UPDATED_DATE_REALISATION);
        assertThat(testWoArticles.getQteCommande()).isEqualTo(DEFAULT_QTE_COMMANDE);
        assertThat(testWoArticles.getQteEffectuee()).isEqualTo(DEFAULT_QTE_EFFECTUEE);
        assertThat(testWoArticles.getPrixArticle()).isEqualByComparingTo(UPDATED_PRIX_ARTICLE);
        assertThat(testWoArticles.getPrixArticleHT()).isEqualByComparingTo(DEFAULT_PRIX_ARTICLE_HT);
    }

    @Test
    @Transactional
    void fullUpdateWoArticlesWithPatch() throws Exception {
        // Initialize the database
        woArticlesRepository.saveAndFlush(woArticles);

        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();

        // Update the woArticles using partial update
        WoArticles partialUpdatedWoArticles = new WoArticles();
        partialUpdatedWoArticles.setId(woArticles.getId());

        partialUpdatedWoArticles
            .woId(UPDATED_WO_ID)
            .articleId(UPDATED_ARTICLE_ID)
            .dateRealisation(UPDATED_DATE_REALISATION)
            .qteCommande(UPDATED_QTE_COMMANDE)
            .qteEffectuee(UPDATED_QTE_EFFECTUEE)
            .prixArticle(UPDATED_PRIX_ARTICLE)
            .prixArticleHT(UPDATED_PRIX_ARTICLE_HT);

        restWoArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWoArticles.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWoArticles))
            )
            .andExpect(status().isOk());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
        WoArticles testWoArticles = woArticlesList.get(woArticlesList.size() - 1);
        assertThat(testWoArticles.getWoId()).isEqualTo(UPDATED_WO_ID);
        assertThat(testWoArticles.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testWoArticles.getDateRealisation()).isEqualTo(UPDATED_DATE_REALISATION);
        assertThat(testWoArticles.getQteCommande()).isEqualTo(UPDATED_QTE_COMMANDE);
        assertThat(testWoArticles.getQteEffectuee()).isEqualTo(UPDATED_QTE_EFFECTUEE);
        assertThat(testWoArticles.getPrixArticle()).isEqualByComparingTo(UPDATED_PRIX_ARTICLE);
        assertThat(testWoArticles.getPrixArticleHT()).isEqualByComparingTo(UPDATED_PRIX_ARTICLE_HT);
    }

    @Test
    @Transactional
    void patchNonExistingWoArticles() throws Exception {
        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();
        woArticles.setId(count.incrementAndGet());

        // Create the WoArticles
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(woArticles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWoArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, woArticlesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWoArticles() throws Exception {
        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();
        woArticles.setId(count.incrementAndGet());

        // Create the WoArticles
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(woArticles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWoArticles() throws Exception {
        int databaseSizeBeforeUpdate = woArticlesRepository.findAll().size();
        woArticles.setId(count.incrementAndGet());

        // Create the WoArticles
        WoArticlesDTO woArticlesDTO = woArticlesMapper.toDto(woArticles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woArticlesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WoArticles in the database
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWoArticles() throws Exception {
        // Initialize the database
        woArticlesRepository.saveAndFlush(woArticles);

        int databaseSizeBeforeDelete = woArticlesRepository.findAll().size();

        // Delete the woArticles
        restWoArticlesMockMvc
            .perform(delete(ENTITY_API_URL_ID, woArticles.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WoArticles> woArticlesList = woArticlesRepository.findAll();
        assertThat(woArticlesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
