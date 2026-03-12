package com.gpm.operations.web.rest;

import static com.gpm.operations.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.Motif;
import com.gpm.operations.repository.MotifRepository;
import com.gpm.operations.service.dto.MotifDTO;
import com.gpm.operations.service.mapper.MotifMapper;
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
 * Integration tests for the {@link MotifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MotifResourceIT {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/motifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MotifRepository motifRepository;

    @Autowired
    private MotifMapper motifMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMotifMockMvc;

    private Motif motif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Motif createEntity(EntityManager em) {
        Motif motif = new Motif()
            .designation(DEFAULT_DESIGNATION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdByUserLogin(DEFAULT_CREATED_BY_USER_LOGIN)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedByUserLogin(DEFAULT_UPDATED_BY_USER_LOGIN);
        return motif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Motif createUpdatedEntity(EntityManager em) {
        Motif motif = new Motif()
            .designation(UPDATED_DESIGNATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        return motif;
    }

    @BeforeEach
    public void initTest() {
        motif = createEntity(em);
    }

    @Test
    @Transactional
    void createMotif() throws Exception {
        int databaseSizeBeforeCreate = motifRepository.findAll().size();
        // Create the Motif
        MotifDTO motifDTO = motifMapper.toDto(motif);
        restMotifMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeCreate + 1);
        Motif testMotif = motifList.get(motifList.size() - 1);
        assertThat(testMotif.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testMotif.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMotif.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testMotif.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMotif.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testMotif.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testMotif.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void createMotifWithExistingId() throws Exception {
        // Create the Motif with an existing ID
        motif.setId(1L);
        MotifDTO motifDTO = motifMapper.toDto(motif);

        int databaseSizeBeforeCreate = motifRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotifMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = motifRepository.findAll().size();
        // set the field null
        motif.setDesignation(null);

        // Create the Motif, which fails.
        MotifDTO motifDTO = motifMapper.toDto(motif);

        restMotifMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isBadRequest());

        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMotifs() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        // Get all the motifList
        restMotifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motif.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdByUserLogin").value(hasItem(DEFAULT_CREATED_BY_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedByUserLogin").value(hasItem(DEFAULT_UPDATED_BY_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getMotif() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        // Get the motif
        restMotifMockMvc
            .perform(get(ENTITY_API_URL_ID, motif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(motif.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdByUserLogin").value(DEFAULT_CREATED_BY_USER_LOGIN))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedByUserLogin").value(DEFAULT_UPDATED_BY_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingMotif() throws Exception {
        // Get the motif
        restMotifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMotif() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        int databaseSizeBeforeUpdate = motifRepository.findAll().size();

        // Update the motif
        Motif updatedMotif = motifRepository.findById(motif.getId()).get();
        // Disconnect from session so that the updates on updatedMotif are not directly saved in db
        em.detach(updatedMotif);
        updatedMotif
            .designation(UPDATED_DESIGNATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        MotifDTO motifDTO = motifMapper.toDto(updatedMotif);

        restMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motifDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isOk());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
        Motif testMotif = motifList.get(motifList.size() - 1);
        assertThat(testMotif.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testMotif.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMotif.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testMotif.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMotif.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testMotif.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testMotif.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // Create the Motif
        MotifDTO motifDTO = motifMapper.toDto(motif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motifDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // Create the Motif
        MotifDTO motifDTO = motifMapper.toDto(motif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // Create the Motif
        MotifDTO motifDTO = motifMapper.toDto(motif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMotifWithPatch() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        int databaseSizeBeforeUpdate = motifRepository.findAll().size();

        // Update the motif using partial update
        Motif partialUpdatedMotif = new Motif();
        partialUpdatedMotif.setId(motif.getId());

        partialUpdatedMotif.updatedAt(UPDATED_UPDATED_AT).createdBy(UPDATED_CREATED_BY).updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotif.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotif))
            )
            .andExpect(status().isOk());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
        Motif testMotif = motifList.get(motifList.size() - 1);
        assertThat(testMotif.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testMotif.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMotif.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testMotif.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMotif.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testMotif.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testMotif.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateMotifWithPatch() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        int databaseSizeBeforeUpdate = motifRepository.findAll().size();

        // Update the motif using partial update
        Motif partialUpdatedMotif = new Motif();
        partialUpdatedMotif.setId(motif.getId());

        partialUpdatedMotif
            .designation(UPDATED_DESIGNATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotif.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotif))
            )
            .andExpect(status().isOk());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
        Motif testMotif = motifList.get(motifList.size() - 1);
        assertThat(testMotif.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testMotif.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMotif.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testMotif.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMotif.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testMotif.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testMotif.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // Create the Motif
        MotifDTO motifDTO = motifMapper.toDto(motif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, motifDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // Create the Motif
        MotifDTO motifDTO = motifMapper.toDto(motif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // Create the Motif
        MotifDTO motifDTO = motifMapper.toDto(motif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMotif() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        int databaseSizeBeforeDelete = motifRepository.findAll().size();

        // Delete the motif
        restMotifMockMvc
            .perform(delete(ENTITY_API_URL_ID, motif.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
