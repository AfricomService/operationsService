package com.gpm.operations.web.rest;

import static com.gpm.operations.web.rest.TestUtil.sameInstant;
import static com.gpm.operations.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.NoteFrais;
import com.gpm.operations.repository.NoteFraisRepository;
import com.gpm.operations.service.dto.NoteFraisDTO;
import com.gpm.operations.service.mapper.NoteFraisMapper;
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
 * Integration tests for the {@link NoteFraisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NoteFraisResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CONTACT_SOCIETE_ID = 1L;
    private static final Long UPDATED_CONTACT_SOCIETE_ID = 2L;

    private static final BigDecimal DEFAULT_MONTANT_TOTALE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_TOTALE = new BigDecimal(2);

    private static final String DEFAULT_MONTANT_TOTALE_EN_LETTRE = "AAAAAAAAAA";
    private static final String UPDATED_MONTANT_TOTALE_EN_LETTRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/note-frais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NoteFraisRepository noteFraisRepository;

    @Autowired
    private NoteFraisMapper noteFraisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNoteFraisMockMvc;

    private NoteFrais noteFrais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NoteFrais createEntity(EntityManager em) {
        NoteFrais noteFrais = new NoteFrais()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .contactSocieteId(DEFAULT_CONTACT_SOCIETE_ID)
            .montantTotale(DEFAULT_MONTANT_TOTALE)
            .montantTotaleEnLettre(DEFAULT_MONTANT_TOTALE_EN_LETTRE);
        return noteFrais;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NoteFrais createUpdatedEntity(EntityManager em) {
        NoteFrais noteFrais = new NoteFrais()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID)
            .montantTotale(UPDATED_MONTANT_TOTALE)
            .montantTotaleEnLettre(UPDATED_MONTANT_TOTALE_EN_LETTRE);
        return noteFrais;
    }

    @BeforeEach
    public void initTest() {
        noteFrais = createEntity(em);
    }

    @Test
    @Transactional
    void createNoteFrais() throws Exception {
        int databaseSizeBeforeCreate = noteFraisRepository.findAll().size();
        // Create the NoteFrais
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(noteFrais);
        restNoteFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeCreate + 1);
        NoteFrais testNoteFrais = noteFraisList.get(noteFraisList.size() - 1);
        assertThat(testNoteFrais.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testNoteFrais.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testNoteFrais.getContactSocieteId()).isEqualTo(DEFAULT_CONTACT_SOCIETE_ID);
        assertThat(testNoteFrais.getMontantTotale()).isEqualByComparingTo(DEFAULT_MONTANT_TOTALE);
        assertThat(testNoteFrais.getMontantTotaleEnLettre()).isEqualTo(DEFAULT_MONTANT_TOTALE_EN_LETTRE);
    }

    @Test
    @Transactional
    void createNoteFraisWithExistingId() throws Exception {
        // Create the NoteFrais with an existing ID
        noteFrais.setId(1L);
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(noteFrais);

        int databaseSizeBeforeCreate = noteFraisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNoteFrais() throws Exception {
        // Initialize the database
        noteFraisRepository.saveAndFlush(noteFrais);

        // Get all the noteFraisList
        restNoteFraisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(noteFrais.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].contactSocieteId").value(hasItem(DEFAULT_CONTACT_SOCIETE_ID.intValue())))
            .andExpect(jsonPath("$.[*].montantTotale").value(hasItem(sameNumber(DEFAULT_MONTANT_TOTALE))))
            .andExpect(jsonPath("$.[*].montantTotaleEnLettre").value(hasItem(DEFAULT_MONTANT_TOTALE_EN_LETTRE)));
    }

    @Test
    @Transactional
    void getNoteFrais() throws Exception {
        // Initialize the database
        noteFraisRepository.saveAndFlush(noteFrais);

        // Get the noteFrais
        restNoteFraisMockMvc
            .perform(get(ENTITY_API_URL_ID, noteFrais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(noteFrais.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(sameInstant(DEFAULT_DATE_DEBUT)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)))
            .andExpect(jsonPath("$.contactSocieteId").value(DEFAULT_CONTACT_SOCIETE_ID.intValue()))
            .andExpect(jsonPath("$.montantTotale").value(sameNumber(DEFAULT_MONTANT_TOTALE)))
            .andExpect(jsonPath("$.montantTotaleEnLettre").value(DEFAULT_MONTANT_TOTALE_EN_LETTRE));
    }

    @Test
    @Transactional
    void getNonExistingNoteFrais() throws Exception {
        // Get the noteFrais
        restNoteFraisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNoteFrais() throws Exception {
        // Initialize the database
        noteFraisRepository.saveAndFlush(noteFrais);

        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();

        // Update the noteFrais
        NoteFrais updatedNoteFrais = noteFraisRepository.findById(noteFrais.getId()).get();
        // Disconnect from session so that the updates on updatedNoteFrais are not directly saved in db
        em.detach(updatedNoteFrais);
        updatedNoteFrais
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID)
            .montantTotale(UPDATED_MONTANT_TOTALE)
            .montantTotaleEnLettre(UPDATED_MONTANT_TOTALE_EN_LETTRE);
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(updatedNoteFrais);

        restNoteFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, noteFraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isOk());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
        NoteFrais testNoteFrais = noteFraisList.get(noteFraisList.size() - 1);
        assertThat(testNoteFrais.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testNoteFrais.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testNoteFrais.getContactSocieteId()).isEqualTo(UPDATED_CONTACT_SOCIETE_ID);
        assertThat(testNoteFrais.getMontantTotale()).isEqualTo(UPDATED_MONTANT_TOTALE);
        assertThat(testNoteFrais.getMontantTotaleEnLettre()).isEqualTo(UPDATED_MONTANT_TOTALE_EN_LETTRE);
    }

    @Test
    @Transactional
    void putNonExistingNoteFrais() throws Exception {
        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();
        noteFrais.setId(count.incrementAndGet());

        // Create the NoteFrais
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(noteFrais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, noteFraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNoteFrais() throws Exception {
        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();
        noteFrais.setId(count.incrementAndGet());

        // Create the NoteFrais
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(noteFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNoteFrais() throws Exception {
        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();
        noteFrais.setId(count.incrementAndGet());

        // Create the NoteFrais
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(noteFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteFraisMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNoteFraisWithPatch() throws Exception {
        // Initialize the database
        noteFraisRepository.saveAndFlush(noteFrais);

        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();

        // Update the noteFrais using partial update
        NoteFrais partialUpdatedNoteFrais = new NoteFrais();
        partialUpdatedNoteFrais.setId(noteFrais.getId());

        partialUpdatedNoteFrais
            .dateDebut(UPDATED_DATE_DEBUT)
            .montantTotale(UPDATED_MONTANT_TOTALE)
            .montantTotaleEnLettre(UPDATED_MONTANT_TOTALE_EN_LETTRE);

        restNoteFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNoteFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNoteFrais))
            )
            .andExpect(status().isOk());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
        NoteFrais testNoteFrais = noteFraisList.get(noteFraisList.size() - 1);
        assertThat(testNoteFrais.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testNoteFrais.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testNoteFrais.getContactSocieteId()).isEqualTo(DEFAULT_CONTACT_SOCIETE_ID);
        assertThat(testNoteFrais.getMontantTotale()).isEqualByComparingTo(UPDATED_MONTANT_TOTALE);
        assertThat(testNoteFrais.getMontantTotaleEnLettre()).isEqualTo(UPDATED_MONTANT_TOTALE_EN_LETTRE);
    }

    @Test
    @Transactional
    void fullUpdateNoteFraisWithPatch() throws Exception {
        // Initialize the database
        noteFraisRepository.saveAndFlush(noteFrais);

        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();

        // Update the noteFrais using partial update
        NoteFrais partialUpdatedNoteFrais = new NoteFrais();
        partialUpdatedNoteFrais.setId(noteFrais.getId());

        partialUpdatedNoteFrais
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID)
            .montantTotale(UPDATED_MONTANT_TOTALE)
            .montantTotaleEnLettre(UPDATED_MONTANT_TOTALE_EN_LETTRE);

        restNoteFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNoteFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNoteFrais))
            )
            .andExpect(status().isOk());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
        NoteFrais testNoteFrais = noteFraisList.get(noteFraisList.size() - 1);
        assertThat(testNoteFrais.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testNoteFrais.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testNoteFrais.getContactSocieteId()).isEqualTo(UPDATED_CONTACT_SOCIETE_ID);
        assertThat(testNoteFrais.getMontantTotale()).isEqualByComparingTo(UPDATED_MONTANT_TOTALE);
        assertThat(testNoteFrais.getMontantTotaleEnLettre()).isEqualTo(UPDATED_MONTANT_TOTALE_EN_LETTRE);
    }

    @Test
    @Transactional
    void patchNonExistingNoteFrais() throws Exception {
        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();
        noteFrais.setId(count.incrementAndGet());

        // Create the NoteFrais
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(noteFrais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, noteFraisDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNoteFrais() throws Exception {
        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();
        noteFrais.setId(count.incrementAndGet());

        // Create the NoteFrais
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(noteFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNoteFrais() throws Exception {
        int databaseSizeBeforeUpdate = noteFraisRepository.findAll().size();
        noteFrais.setId(count.incrementAndGet());

        // Create the NoteFrais
        NoteFraisDTO noteFraisDTO = noteFraisMapper.toDto(noteFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteFraisMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(noteFraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NoteFrais in the database
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNoteFrais() throws Exception {
        // Initialize the database
        noteFraisRepository.saveAndFlush(noteFrais);

        int databaseSizeBeforeDelete = noteFraisRepository.findAll().size();

        // Delete the noteFrais
        restNoteFraisMockMvc
            .perform(delete(ENTITY_API_URL_ID, noteFrais.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NoteFrais> noteFraisList = noteFraisRepository.findAll();
        assertThat(noteFraisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
