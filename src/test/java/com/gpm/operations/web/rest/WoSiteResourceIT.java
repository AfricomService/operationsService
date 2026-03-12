package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.WoSite;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.repository.WoSiteRepository;
import com.gpm.operations.service.dto.WoSiteDTO;
import com.gpm.operations.service.mapper.WoSiteMapper;
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
 * Integration tests for the {@link WoSiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WoSiteResourceIT {

    private static final String DEFAULT_SITE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SITE_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wo-sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WoSiteRepository woSiteRepository;

    @Autowired
    private WoSiteMapper woSiteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWoSiteMockMvc;

    private WoSite woSite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WoSite createEntity(EntityManager em) {
        WoSite woSite = new WoSite().siteCode(DEFAULT_SITE_CODE);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        woSite.setWorkOrder(workOrder);
        return woSite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WoSite createUpdatedEntity(EntityManager em) {
        WoSite woSite = new WoSite().siteCode(UPDATED_SITE_CODE);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createUpdatedEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        woSite.setWorkOrder(workOrder);
        return woSite;
    }

    @BeforeEach
    public void initTest() {
        woSite = createEntity(em);
    }

    @Test
    @Transactional
    void createWoSite() throws Exception {
        int databaseSizeBeforeCreate = woSiteRepository.findAll().size();
        // Create the WoSite
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(woSite);
        restWoSiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeCreate + 1);
        WoSite testWoSite = woSiteList.get(woSiteList.size() - 1);
        assertThat(testWoSite.getSiteCode()).isEqualTo(DEFAULT_SITE_CODE);
    }

    @Test
    @Transactional
    void createWoSiteWithExistingId() throws Exception {
        // Create the WoSite with an existing ID
        woSite.setId(1L);
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(woSite);

        int databaseSizeBeforeCreate = woSiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWoSiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWoSites() throws Exception {
        // Initialize the database
        woSiteRepository.saveAndFlush(woSite);

        // Get all the woSiteList
        restWoSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(woSite.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteCode").value(hasItem(DEFAULT_SITE_CODE)));
    }

    @Test
    @Transactional
    void getWoSite() throws Exception {
        // Initialize the database
        woSiteRepository.saveAndFlush(woSite);

        // Get the woSite
        restWoSiteMockMvc
            .perform(get(ENTITY_API_URL_ID, woSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(woSite.getId().intValue()))
            .andExpect(jsonPath("$.siteCode").value(DEFAULT_SITE_CODE));
    }

    @Test
    @Transactional
    void getNonExistingWoSite() throws Exception {
        // Get the woSite
        restWoSiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWoSite() throws Exception {
        // Initialize the database
        woSiteRepository.saveAndFlush(woSite);

        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();

        // Update the woSite
        WoSite updatedWoSite = woSiteRepository.findById(woSite.getId()).get();
        // Disconnect from session so that the updates on updatedWoSite are not directly saved in db
        em.detach(updatedWoSite);
        updatedWoSite.siteCode(UPDATED_SITE_CODE);
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(updatedWoSite);

        restWoSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, woSiteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
        WoSite testWoSite = woSiteList.get(woSiteList.size() - 1);
        assertThat(testWoSite.getSiteCode()).isEqualTo(UPDATED_SITE_CODE);
    }

    @Test
    @Transactional
    void putNonExistingWoSite() throws Exception {
        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();
        woSite.setId(count.incrementAndGet());

        // Create the WoSite
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(woSite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWoSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, woSiteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWoSite() throws Exception {
        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();
        woSite.setId(count.incrementAndGet());

        // Create the WoSite
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(woSite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWoSite() throws Exception {
        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();
        woSite.setId(count.incrementAndGet());

        // Create the WoSite
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(woSite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoSiteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWoSiteWithPatch() throws Exception {
        // Initialize the database
        woSiteRepository.saveAndFlush(woSite);

        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();

        // Update the woSite using partial update
        WoSite partialUpdatedWoSite = new WoSite();
        partialUpdatedWoSite.setId(woSite.getId());

        restWoSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWoSite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWoSite))
            )
            .andExpect(status().isOk());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
        WoSite testWoSite = woSiteList.get(woSiteList.size() - 1);
        assertThat(testWoSite.getSiteCode()).isEqualTo(DEFAULT_SITE_CODE);
    }

    @Test
    @Transactional
    void fullUpdateWoSiteWithPatch() throws Exception {
        // Initialize the database
        woSiteRepository.saveAndFlush(woSite);

        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();

        // Update the woSite using partial update
        WoSite partialUpdatedWoSite = new WoSite();
        partialUpdatedWoSite.setId(woSite.getId());

        partialUpdatedWoSite.siteCode(UPDATED_SITE_CODE);

        restWoSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWoSite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWoSite))
            )
            .andExpect(status().isOk());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
        WoSite testWoSite = woSiteList.get(woSiteList.size() - 1);
        assertThat(testWoSite.getSiteCode()).isEqualTo(UPDATED_SITE_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingWoSite() throws Exception {
        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();
        woSite.setId(count.incrementAndGet());

        // Create the WoSite
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(woSite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWoSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, woSiteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWoSite() throws Exception {
        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();
        woSite.setId(count.incrementAndGet());

        // Create the WoSite
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(woSite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWoSite() throws Exception {
        int databaseSizeBeforeUpdate = woSiteRepository.findAll().size();
        woSite.setId(count.incrementAndGet());

        // Create the WoSite
        WoSiteDTO woSiteDTO = woSiteMapper.toDto(woSite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoSiteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woSiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WoSite in the database
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWoSite() throws Exception {
        // Initialize the database
        woSiteRepository.saveAndFlush(woSite);

        int databaseSizeBeforeDelete = woSiteRepository.findAll().size();

        // Delete the woSite
        restWoSiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, woSite.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WoSite> woSiteList = woSiteRepository.findAll();
        assertThat(woSiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
