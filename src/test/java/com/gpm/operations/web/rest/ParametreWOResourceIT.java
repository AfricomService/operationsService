package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.ParametreWO;
import com.gpm.operations.repository.ParametreWORepository;
import com.gpm.operations.service.dto.ParametreWODTO;
import com.gpm.operations.service.mapper.ParametreWOMapper;
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
 * Integration tests for the {@link ParametreWOResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParametreWOResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parametre-wos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParametreWORepository parametreWORepository;

    @Autowired
    private ParametreWOMapper parametreWOMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParametreWOMockMvc;

    private ParametreWO parametreWO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParametreWO createEntity(EntityManager em) {
        ParametreWO parametreWO = new ParametreWO().nom(DEFAULT_NOM).description(DEFAULT_DESCRIPTION);
        return parametreWO;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParametreWO createUpdatedEntity(EntityManager em) {
        ParametreWO parametreWO = new ParametreWO().nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);
        return parametreWO;
    }

    @BeforeEach
    public void initTest() {
        parametreWO = createEntity(em);
    }

    @Test
    @Transactional
    void createParametreWO() throws Exception {
        int databaseSizeBeforeCreate = parametreWORepository.findAll().size();
        // Create the ParametreWO
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(parametreWO);
        restParametreWOMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isCreated());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeCreate + 1);
        ParametreWO testParametreWO = parametreWOList.get(parametreWOList.size() - 1);
        assertThat(testParametreWO.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testParametreWO.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createParametreWOWithExistingId() throws Exception {
        // Create the ParametreWO with an existing ID
        parametreWO.setId(1L);
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(parametreWO);

        int databaseSizeBeforeCreate = parametreWORepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParametreWOMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParametreWOS() throws Exception {
        // Initialize the database
        parametreWORepository.saveAndFlush(parametreWO);

        // Get all the parametreWOList
        restParametreWOMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametreWO.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getParametreWO() throws Exception {
        // Initialize the database
        parametreWORepository.saveAndFlush(parametreWO);

        // Get the parametreWO
        restParametreWOMockMvc
            .perform(get(ENTITY_API_URL_ID, parametreWO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parametreWO.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingParametreWO() throws Exception {
        // Get the parametreWO
        restParametreWOMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParametreWO() throws Exception {
        // Initialize the database
        parametreWORepository.saveAndFlush(parametreWO);

        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();

        // Update the parametreWO
        ParametreWO updatedParametreWO = parametreWORepository.findById(parametreWO.getId()).get();
        // Disconnect from session so that the updates on updatedParametreWO are not directly saved in db
        em.detach(updatedParametreWO);
        updatedParametreWO.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(updatedParametreWO);

        restParametreWOMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametreWODTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isOk());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
        ParametreWO testParametreWO = parametreWOList.get(parametreWOList.size() - 1);
        assertThat(testParametreWO.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testParametreWO.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingParametreWO() throws Exception {
        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();
        parametreWO.setId(count.incrementAndGet());

        // Create the ParametreWO
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(parametreWO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametreWOMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametreWODTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParametreWO() throws Exception {
        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();
        parametreWO.setId(count.incrementAndGet());

        // Create the ParametreWO
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(parametreWO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreWOMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParametreWO() throws Exception {
        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();
        parametreWO.setId(count.incrementAndGet());

        // Create the ParametreWO
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(parametreWO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreWOMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParametreWOWithPatch() throws Exception {
        // Initialize the database
        parametreWORepository.saveAndFlush(parametreWO);

        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();

        // Update the parametreWO using partial update
        ParametreWO partialUpdatedParametreWO = new ParametreWO();
        partialUpdatedParametreWO.setId(parametreWO.getId());

        partialUpdatedParametreWO.description(UPDATED_DESCRIPTION);

        restParametreWOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametreWO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametreWO))
            )
            .andExpect(status().isOk());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
        ParametreWO testParametreWO = parametreWOList.get(parametreWOList.size() - 1);
        assertThat(testParametreWO.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testParametreWO.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateParametreWOWithPatch() throws Exception {
        // Initialize the database
        parametreWORepository.saveAndFlush(parametreWO);

        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();

        // Update the parametreWO using partial update
        ParametreWO partialUpdatedParametreWO = new ParametreWO();
        partialUpdatedParametreWO.setId(parametreWO.getId());

        partialUpdatedParametreWO.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);

        restParametreWOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametreWO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametreWO))
            )
            .andExpect(status().isOk());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
        ParametreWO testParametreWO = parametreWOList.get(parametreWOList.size() - 1);
        assertThat(testParametreWO.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testParametreWO.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingParametreWO() throws Exception {
        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();
        parametreWO.setId(count.incrementAndGet());

        // Create the ParametreWO
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(parametreWO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametreWOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parametreWODTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParametreWO() throws Exception {
        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();
        parametreWO.setId(count.incrementAndGet());

        // Create the ParametreWO
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(parametreWO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreWOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParametreWO() throws Exception {
        int databaseSizeBeforeUpdate = parametreWORepository.findAll().size();
        parametreWO.setId(count.incrementAndGet());

        // Create the ParametreWO
        ParametreWODTO parametreWODTO = parametreWOMapper.toDto(parametreWO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreWOMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametreWODTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParametreWO in the database
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParametreWO() throws Exception {
        // Initialize the database
        parametreWORepository.saveAndFlush(parametreWO);

        int databaseSizeBeforeDelete = parametreWORepository.findAll().size();

        // Delete the parametreWO
        restParametreWOMockMvc
            .perform(delete(ENTITY_API_URL_ID, parametreWO.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParametreWO> parametreWOList = parametreWORepository.findAll();
        assertThat(parametreWOList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
