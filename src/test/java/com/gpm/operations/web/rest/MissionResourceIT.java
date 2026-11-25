package com.gpm.operations.web.rest;

import static com.gpm.operations.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.Mission;
import com.gpm.operations.repository.MissionRepository;
import com.gpm.operations.service.criteria.MissionCriteria;
import com.gpm.operations.service.dto.MissionDTO;
import com.gpm.operations.service.mapper.MissionMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link MissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MissionResourceIT {

    private static final String DEFAULT_TITRE_BENEFICIAIRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE_BENEFICIAIRE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_NOTE_MISSION = new BigDecimal(1);
    private static final BigDecimal UPDATED_NOTE_MISSION = new BigDecimal(2);
    private static final BigDecimal SMALLER_NOTE_MISSION = new BigDecimal(1 - 1);

    private static final Long DEFAULT_CONTACT_SOCIETE_ID = 1L;
    private static final Long UPDATED_CONTACT_SOCIETE_ID = 2L;
    private static final Long SMALLER_CONTACT_SOCIETE_ID = 1L - 1L;

    private static final String DEFAULT_CONTACT_SOCIETE_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_SOCIETE_MATRICULE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_FRAIS_JOUR_FERIE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FRAIS_JOUR_FERIE = new BigDecimal(2);
    private static final BigDecimal SMALLER_FRAIS_JOUR_FERIE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_FRAIS_JOUR_WEEKEND = new BigDecimal(1);
    private static final BigDecimal UPDATED_FRAIS_JOUR_WEEKEND = new BigDecimal(2);
    private static final BigDecimal SMALLER_FRAIS_JOUR_WEEKEND = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_FRAIS_PERSONNEL = new BigDecimal(1);
    private static final BigDecimal UPDATED_FRAIS_PERSONNEL = new BigDecimal(2);
    private static final BigDecimal SMALLER_FRAIS_PERSONNEL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MONTANT_TOTALE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_TOTALE = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT_TOTALE = new BigDecimal(1 - 1);

    private static final Long DEFAULT_WORKORDER_ID = 1L;
    private static final Long UPDATED_WORKORDER_ID = 2L;
    private static final Long SMALLER_WORKORDER_ID = 1L - 1L;

    private static final Long DEFAULT_NOTE_FRAIS_ID = 1L;
    private static final Long UPDATED_NOTE_FRAIS_ID = 2L;
    private static final Long SMALLER_NOTE_FRAIS_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/missions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMissionMockMvc;

    private Mission mission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mission createEntity(EntityManager em) {
        Mission mission = new Mission()
            .titreBeneficiaire(DEFAULT_TITRE_BENEFICIAIRE)
            .noteMission(DEFAULT_NOTE_MISSION)
            .contactSocieteId(DEFAULT_CONTACT_SOCIETE_ID)
            .contactSocieteMatricule(DEFAULT_CONTACT_SOCIETE_MATRICULE)
            .fraisJourFerie(DEFAULT_FRAIS_JOUR_FERIE)
            .fraisJourWeekend(DEFAULT_FRAIS_JOUR_WEEKEND)
            .fraisPersonnel(DEFAULT_FRAIS_PERSONNEL)
            .montantTotale(DEFAULT_MONTANT_TOTALE)
            .workorderId(DEFAULT_WORKORDER_ID)
            .noteFraisId(DEFAULT_NOTE_FRAIS_ID);
        return mission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mission createUpdatedEntity(EntityManager em) {
        Mission mission = new Mission()
            .titreBeneficiaire(UPDATED_TITRE_BENEFICIAIRE)
            .noteMission(UPDATED_NOTE_MISSION)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID)
            .contactSocieteMatricule(UPDATED_CONTACT_SOCIETE_MATRICULE)
            .fraisJourFerie(UPDATED_FRAIS_JOUR_FERIE)
            .fraisJourWeekend(UPDATED_FRAIS_JOUR_WEEKEND)
            .fraisPersonnel(UPDATED_FRAIS_PERSONNEL)
            .montantTotale(UPDATED_MONTANT_TOTALE)
            .workorderId(UPDATED_WORKORDER_ID)
            .noteFraisId(UPDATED_NOTE_FRAIS_ID);
        return mission;
    }

    @BeforeEach
    public void initTest() {
        mission = createEntity(em);
    }

    @Test
    @Transactional
    void createMission() throws Exception {
        int databaseSizeBeforeCreate = missionRepository.findAll().size();
        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);
        restMissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeCreate + 1);
        Mission testMission = missionList.get(missionList.size() - 1);
        assertThat(testMission.getTitreBeneficiaire()).isEqualTo(DEFAULT_TITRE_BENEFICIAIRE);
        assertThat(testMission.getNoteMission()).isEqualByComparingTo(DEFAULT_NOTE_MISSION);
        assertThat(testMission.getContactSocieteId()).isEqualTo(DEFAULT_CONTACT_SOCIETE_ID);
        assertThat(testMission.getContactSocieteMatricule()).isEqualTo(DEFAULT_CONTACT_SOCIETE_MATRICULE);
        assertThat(testMission.getFraisJourFerie()).isEqualByComparingTo(DEFAULT_FRAIS_JOUR_FERIE);
        assertThat(testMission.getFraisJourWeekend()).isEqualByComparingTo(DEFAULT_FRAIS_JOUR_WEEKEND);
        assertThat(testMission.getFraisPersonnel()).isEqualByComparingTo(DEFAULT_FRAIS_PERSONNEL);
        assertThat(testMission.getMontantTotale()).isEqualByComparingTo(DEFAULT_MONTANT_TOTALE);
        assertThat(testMission.getWorkorderId()).isEqualTo(DEFAULT_WORKORDER_ID);
        assertThat(testMission.getNoteFraisId()).isEqualTo(DEFAULT_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void createMissionWithExistingId() throws Exception {
        // Create the Mission with an existing ID
        mission.setId(1L);
        MissionDTO missionDTO = missionMapper.toDto(mission);

        int databaseSizeBeforeCreate = missionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMissions() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList
        restMissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mission.getId().intValue())))
            .andExpect(jsonPath("$.[*].titreBeneficiaire").value(hasItem(DEFAULT_TITRE_BENEFICIAIRE)))
            .andExpect(jsonPath("$.[*].noteMission").value(hasItem(sameNumber(DEFAULT_NOTE_MISSION))))
            .andExpect(jsonPath("$.[*].contactSocieteId").value(hasItem(DEFAULT_CONTACT_SOCIETE_ID.intValue())))
            .andExpect(jsonPath("$.[*].contactSocieteMatricule").value(hasItem(DEFAULT_CONTACT_SOCIETE_MATRICULE)))
            .andExpect(jsonPath("$.[*].fraisJourFerie").value(hasItem(sameNumber(DEFAULT_FRAIS_JOUR_FERIE))))
            .andExpect(jsonPath("$.[*].fraisJourWeekend").value(hasItem(sameNumber(DEFAULT_FRAIS_JOUR_WEEKEND))))
            .andExpect(jsonPath("$.[*].fraisPersonnel").value(hasItem(sameNumber(DEFAULT_FRAIS_PERSONNEL))))
            .andExpect(jsonPath("$.[*].montantTotale").value(hasItem(sameNumber(DEFAULT_MONTANT_TOTALE))))
            .andExpect(jsonPath("$.[*].workorderId").value(hasItem(DEFAULT_WORKORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].noteFraisId").value(hasItem(DEFAULT_NOTE_FRAIS_ID.intValue())));
    }

    @Test
    @Transactional
    void getMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get the mission
        restMissionMockMvc
            .perform(get(ENTITY_API_URL_ID, mission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mission.getId().intValue()))
            .andExpect(jsonPath("$.titreBeneficiaire").value(DEFAULT_TITRE_BENEFICIAIRE))
            .andExpect(jsonPath("$.noteMission").value(sameNumber(DEFAULT_NOTE_MISSION)))
            .andExpect(jsonPath("$.contactSocieteId").value(DEFAULT_CONTACT_SOCIETE_ID.intValue()))
            .andExpect(jsonPath("$.contactSocieteMatricule").value(DEFAULT_CONTACT_SOCIETE_MATRICULE))
            .andExpect(jsonPath("$.fraisJourFerie").value(sameNumber(DEFAULT_FRAIS_JOUR_FERIE)))
            .andExpect(jsonPath("$.fraisJourWeekend").value(sameNumber(DEFAULT_FRAIS_JOUR_WEEKEND)))
            .andExpect(jsonPath("$.fraisPersonnel").value(sameNumber(DEFAULT_FRAIS_PERSONNEL)))
            .andExpect(jsonPath("$.montantTotale").value(sameNumber(DEFAULT_MONTANT_TOTALE)))
            .andExpect(jsonPath("$.workorderId").value(DEFAULT_WORKORDER_ID.intValue()))
            .andExpect(jsonPath("$.noteFraisId").value(DEFAULT_NOTE_FRAIS_ID.intValue()));
    }

    @Test
    @Transactional
    void getMissionsByIdFiltering() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        Long id = mission.getId();

        defaultMissionShouldBeFound("id.equals=" + id);
        defaultMissionShouldNotBeFound("id.notEquals=" + id);

        defaultMissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMissionShouldNotBeFound("id.greaterThan=" + id);

        defaultMissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMissionsByTitreBeneficiaireIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where titreBeneficiaire equals to DEFAULT_TITRE_BENEFICIAIRE
        defaultMissionShouldBeFound("titreBeneficiaire.equals=" + DEFAULT_TITRE_BENEFICIAIRE);

        // Get all the missionList where titreBeneficiaire equals to UPDATED_TITRE_BENEFICIAIRE
        defaultMissionShouldNotBeFound("titreBeneficiaire.equals=" + UPDATED_TITRE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllMissionsByTitreBeneficiaireIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where titreBeneficiaire not equals to DEFAULT_TITRE_BENEFICIAIRE
        defaultMissionShouldNotBeFound("titreBeneficiaire.notEquals=" + DEFAULT_TITRE_BENEFICIAIRE);

        // Get all the missionList where titreBeneficiaire not equals to UPDATED_TITRE_BENEFICIAIRE
        defaultMissionShouldBeFound("titreBeneficiaire.notEquals=" + UPDATED_TITRE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllMissionsByTitreBeneficiaireIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where titreBeneficiaire in DEFAULT_TITRE_BENEFICIAIRE or UPDATED_TITRE_BENEFICIAIRE
        defaultMissionShouldBeFound("titreBeneficiaire.in=" + DEFAULT_TITRE_BENEFICIAIRE + "," + UPDATED_TITRE_BENEFICIAIRE);

        // Get all the missionList where titreBeneficiaire equals to UPDATED_TITRE_BENEFICIAIRE
        defaultMissionShouldNotBeFound("titreBeneficiaire.in=" + UPDATED_TITRE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllMissionsByTitreBeneficiaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where titreBeneficiaire is not null
        defaultMissionShouldBeFound("titreBeneficiaire.specified=true");

        // Get all the missionList where titreBeneficiaire is null
        defaultMissionShouldNotBeFound("titreBeneficiaire.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByTitreBeneficiaireContainsSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where titreBeneficiaire contains DEFAULT_TITRE_BENEFICIAIRE
        defaultMissionShouldBeFound("titreBeneficiaire.contains=" + DEFAULT_TITRE_BENEFICIAIRE);

        // Get all the missionList where titreBeneficiaire contains UPDATED_TITRE_BENEFICIAIRE
        defaultMissionShouldNotBeFound("titreBeneficiaire.contains=" + UPDATED_TITRE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllMissionsByTitreBeneficiaireNotContainsSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where titreBeneficiaire does not contain DEFAULT_TITRE_BENEFICIAIRE
        defaultMissionShouldNotBeFound("titreBeneficiaire.doesNotContain=" + DEFAULT_TITRE_BENEFICIAIRE);

        // Get all the missionList where titreBeneficiaire does not contain UPDATED_TITRE_BENEFICIAIRE
        defaultMissionShouldBeFound("titreBeneficiaire.doesNotContain=" + UPDATED_TITRE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteMissionIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteMission equals to DEFAULT_NOTE_MISSION
        defaultMissionShouldBeFound("noteMission.equals=" + DEFAULT_NOTE_MISSION);

        // Get all the missionList where noteMission equals to UPDATED_NOTE_MISSION
        defaultMissionShouldNotBeFound("noteMission.equals=" + UPDATED_NOTE_MISSION);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteMissionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteMission not equals to DEFAULT_NOTE_MISSION
        defaultMissionShouldNotBeFound("noteMission.notEquals=" + DEFAULT_NOTE_MISSION);

        // Get all the missionList where noteMission not equals to UPDATED_NOTE_MISSION
        defaultMissionShouldBeFound("noteMission.notEquals=" + UPDATED_NOTE_MISSION);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteMissionIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteMission in DEFAULT_NOTE_MISSION or UPDATED_NOTE_MISSION
        defaultMissionShouldBeFound("noteMission.in=" + DEFAULT_NOTE_MISSION + "," + UPDATED_NOTE_MISSION);

        // Get all the missionList where noteMission equals to UPDATED_NOTE_MISSION
        defaultMissionShouldNotBeFound("noteMission.in=" + UPDATED_NOTE_MISSION);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteMissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteMission is not null
        defaultMissionShouldBeFound("noteMission.specified=true");

        // Get all the missionList where noteMission is null
        defaultMissionShouldNotBeFound("noteMission.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByNoteMissionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteMission is greater than or equal to DEFAULT_NOTE_MISSION
        defaultMissionShouldBeFound("noteMission.greaterThanOrEqual=" + DEFAULT_NOTE_MISSION);

        // Get all the missionList where noteMission is greater than or equal to UPDATED_NOTE_MISSION
        defaultMissionShouldNotBeFound("noteMission.greaterThanOrEqual=" + UPDATED_NOTE_MISSION);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteMissionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteMission is less than or equal to DEFAULT_NOTE_MISSION
        defaultMissionShouldBeFound("noteMission.lessThanOrEqual=" + DEFAULT_NOTE_MISSION);

        // Get all the missionList where noteMission is less than or equal to SMALLER_NOTE_MISSION
        defaultMissionShouldNotBeFound("noteMission.lessThanOrEqual=" + SMALLER_NOTE_MISSION);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteMissionIsLessThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteMission is less than DEFAULT_NOTE_MISSION
        defaultMissionShouldNotBeFound("noteMission.lessThan=" + DEFAULT_NOTE_MISSION);

        // Get all the missionList where noteMission is less than UPDATED_NOTE_MISSION
        defaultMissionShouldBeFound("noteMission.lessThan=" + UPDATED_NOTE_MISSION);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteMissionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteMission is greater than DEFAULT_NOTE_MISSION
        defaultMissionShouldNotBeFound("noteMission.greaterThan=" + DEFAULT_NOTE_MISSION);

        // Get all the missionList where noteMission is greater than SMALLER_NOTE_MISSION
        defaultMissionShouldBeFound("noteMission.greaterThan=" + SMALLER_NOTE_MISSION);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteId equals to DEFAULT_CONTACT_SOCIETE_ID
        defaultMissionShouldBeFound("contactSocieteId.equals=" + DEFAULT_CONTACT_SOCIETE_ID);

        // Get all the missionList where contactSocieteId equals to UPDATED_CONTACT_SOCIETE_ID
        defaultMissionShouldNotBeFound("contactSocieteId.equals=" + UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteId not equals to DEFAULT_CONTACT_SOCIETE_ID
        defaultMissionShouldNotBeFound("contactSocieteId.notEquals=" + DEFAULT_CONTACT_SOCIETE_ID);

        // Get all the missionList where contactSocieteId not equals to UPDATED_CONTACT_SOCIETE_ID
        defaultMissionShouldBeFound("contactSocieteId.notEquals=" + UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteIdIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteId in DEFAULT_CONTACT_SOCIETE_ID or UPDATED_CONTACT_SOCIETE_ID
        defaultMissionShouldBeFound("contactSocieteId.in=" + DEFAULT_CONTACT_SOCIETE_ID + "," + UPDATED_CONTACT_SOCIETE_ID);

        // Get all the missionList where contactSocieteId equals to UPDATED_CONTACT_SOCIETE_ID
        defaultMissionShouldNotBeFound("contactSocieteId.in=" + UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteId is not null
        defaultMissionShouldBeFound("contactSocieteId.specified=true");

        // Get all the missionList where contactSocieteId is null
        defaultMissionShouldNotBeFound("contactSocieteId.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteId is greater than or equal to DEFAULT_CONTACT_SOCIETE_ID
        defaultMissionShouldBeFound("contactSocieteId.greaterThanOrEqual=" + DEFAULT_CONTACT_SOCIETE_ID);

        // Get all the missionList where contactSocieteId is greater than or equal to UPDATED_CONTACT_SOCIETE_ID
        defaultMissionShouldNotBeFound("contactSocieteId.greaterThanOrEqual=" + UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteId is less than or equal to DEFAULT_CONTACT_SOCIETE_ID
        defaultMissionShouldBeFound("contactSocieteId.lessThanOrEqual=" + DEFAULT_CONTACT_SOCIETE_ID);

        // Get all the missionList where contactSocieteId is less than or equal to SMALLER_CONTACT_SOCIETE_ID
        defaultMissionShouldNotBeFound("contactSocieteId.lessThanOrEqual=" + SMALLER_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteId is less than DEFAULT_CONTACT_SOCIETE_ID
        defaultMissionShouldNotBeFound("contactSocieteId.lessThan=" + DEFAULT_CONTACT_SOCIETE_ID);

        // Get all the missionList where contactSocieteId is less than UPDATED_CONTACT_SOCIETE_ID
        defaultMissionShouldBeFound("contactSocieteId.lessThan=" + UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteId is greater than DEFAULT_CONTACT_SOCIETE_ID
        defaultMissionShouldNotBeFound("contactSocieteId.greaterThan=" + DEFAULT_CONTACT_SOCIETE_ID);

        // Get all the missionList where contactSocieteId is greater than SMALLER_CONTACT_SOCIETE_ID
        defaultMissionShouldBeFound("contactSocieteId.greaterThan=" + SMALLER_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteMatriculeIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteMatricule equals to DEFAULT_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldBeFound("contactSocieteMatricule.equals=" + DEFAULT_CONTACT_SOCIETE_MATRICULE);

        // Get all the missionList where contactSocieteMatricule equals to UPDATED_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldNotBeFound("contactSocieteMatricule.equals=" + UPDATED_CONTACT_SOCIETE_MATRICULE);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteMatriculeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteMatricule not equals to DEFAULT_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldNotBeFound("contactSocieteMatricule.notEquals=" + DEFAULT_CONTACT_SOCIETE_MATRICULE);

        // Get all the missionList where contactSocieteMatricule not equals to UPDATED_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldBeFound("contactSocieteMatricule.notEquals=" + UPDATED_CONTACT_SOCIETE_MATRICULE);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteMatriculeIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteMatricule in DEFAULT_CONTACT_SOCIETE_MATRICULE or UPDATED_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldBeFound(
            "contactSocieteMatricule.in=" + DEFAULT_CONTACT_SOCIETE_MATRICULE + "," + UPDATED_CONTACT_SOCIETE_MATRICULE
        );

        // Get all the missionList where contactSocieteMatricule equals to UPDATED_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldNotBeFound("contactSocieteMatricule.in=" + UPDATED_CONTACT_SOCIETE_MATRICULE);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteMatriculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteMatricule is not null
        defaultMissionShouldBeFound("contactSocieteMatricule.specified=true");

        // Get all the missionList where contactSocieteMatricule is null
        defaultMissionShouldNotBeFound("contactSocieteMatricule.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteMatriculeContainsSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteMatricule contains DEFAULT_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldBeFound("contactSocieteMatricule.contains=" + DEFAULT_CONTACT_SOCIETE_MATRICULE);

        // Get all the missionList where contactSocieteMatricule contains UPDATED_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldNotBeFound("contactSocieteMatricule.contains=" + UPDATED_CONTACT_SOCIETE_MATRICULE);
    }

    @Test
    @Transactional
    void getAllMissionsByContactSocieteMatriculeNotContainsSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where contactSocieteMatricule does not contain DEFAULT_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldNotBeFound("contactSocieteMatricule.doesNotContain=" + DEFAULT_CONTACT_SOCIETE_MATRICULE);

        // Get all the missionList where contactSocieteMatricule does not contain UPDATED_CONTACT_SOCIETE_MATRICULE
        defaultMissionShouldBeFound("contactSocieteMatricule.doesNotContain=" + UPDATED_CONTACT_SOCIETE_MATRICULE);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourFerieIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourFerie equals to DEFAULT_FRAIS_JOUR_FERIE
        defaultMissionShouldBeFound("fraisJourFerie.equals=" + DEFAULT_FRAIS_JOUR_FERIE);

        // Get all the missionList where fraisJourFerie equals to UPDATED_FRAIS_JOUR_FERIE
        defaultMissionShouldNotBeFound("fraisJourFerie.equals=" + UPDATED_FRAIS_JOUR_FERIE);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourFerieIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourFerie not equals to DEFAULT_FRAIS_JOUR_FERIE
        defaultMissionShouldNotBeFound("fraisJourFerie.notEquals=" + DEFAULT_FRAIS_JOUR_FERIE);

        // Get all the missionList where fraisJourFerie not equals to UPDATED_FRAIS_JOUR_FERIE
        defaultMissionShouldBeFound("fraisJourFerie.notEquals=" + UPDATED_FRAIS_JOUR_FERIE);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourFerieIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourFerie in DEFAULT_FRAIS_JOUR_FERIE or UPDATED_FRAIS_JOUR_FERIE
        defaultMissionShouldBeFound("fraisJourFerie.in=" + DEFAULT_FRAIS_JOUR_FERIE + "," + UPDATED_FRAIS_JOUR_FERIE);

        // Get all the missionList where fraisJourFerie equals to UPDATED_FRAIS_JOUR_FERIE
        defaultMissionShouldNotBeFound("fraisJourFerie.in=" + UPDATED_FRAIS_JOUR_FERIE);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourFerieIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourFerie is not null
        defaultMissionShouldBeFound("fraisJourFerie.specified=true");

        // Get all the missionList where fraisJourFerie is null
        defaultMissionShouldNotBeFound("fraisJourFerie.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourFerieIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourFerie is greater than or equal to DEFAULT_FRAIS_JOUR_FERIE
        defaultMissionShouldBeFound("fraisJourFerie.greaterThanOrEqual=" + DEFAULT_FRAIS_JOUR_FERIE);

        // Get all the missionList where fraisJourFerie is greater than or equal to UPDATED_FRAIS_JOUR_FERIE
        defaultMissionShouldNotBeFound("fraisJourFerie.greaterThanOrEqual=" + UPDATED_FRAIS_JOUR_FERIE);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourFerieIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourFerie is less than or equal to DEFAULT_FRAIS_JOUR_FERIE
        defaultMissionShouldBeFound("fraisJourFerie.lessThanOrEqual=" + DEFAULT_FRAIS_JOUR_FERIE);

        // Get all the missionList where fraisJourFerie is less than or equal to SMALLER_FRAIS_JOUR_FERIE
        defaultMissionShouldNotBeFound("fraisJourFerie.lessThanOrEqual=" + SMALLER_FRAIS_JOUR_FERIE);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourFerieIsLessThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourFerie is less than DEFAULT_FRAIS_JOUR_FERIE
        defaultMissionShouldNotBeFound("fraisJourFerie.lessThan=" + DEFAULT_FRAIS_JOUR_FERIE);

        // Get all the missionList where fraisJourFerie is less than UPDATED_FRAIS_JOUR_FERIE
        defaultMissionShouldBeFound("fraisJourFerie.lessThan=" + UPDATED_FRAIS_JOUR_FERIE);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourFerieIsGreaterThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourFerie is greater than DEFAULT_FRAIS_JOUR_FERIE
        defaultMissionShouldNotBeFound("fraisJourFerie.greaterThan=" + DEFAULT_FRAIS_JOUR_FERIE);

        // Get all the missionList where fraisJourFerie is greater than SMALLER_FRAIS_JOUR_FERIE
        defaultMissionShouldBeFound("fraisJourFerie.greaterThan=" + SMALLER_FRAIS_JOUR_FERIE);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourWeekendIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourWeekend equals to DEFAULT_FRAIS_JOUR_WEEKEND
        defaultMissionShouldBeFound("fraisJourWeekend.equals=" + DEFAULT_FRAIS_JOUR_WEEKEND);

        // Get all the missionList where fraisJourWeekend equals to UPDATED_FRAIS_JOUR_WEEKEND
        defaultMissionShouldNotBeFound("fraisJourWeekend.equals=" + UPDATED_FRAIS_JOUR_WEEKEND);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourWeekendIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourWeekend not equals to DEFAULT_FRAIS_JOUR_WEEKEND
        defaultMissionShouldNotBeFound("fraisJourWeekend.notEquals=" + DEFAULT_FRAIS_JOUR_WEEKEND);

        // Get all the missionList where fraisJourWeekend not equals to UPDATED_FRAIS_JOUR_WEEKEND
        defaultMissionShouldBeFound("fraisJourWeekend.notEquals=" + UPDATED_FRAIS_JOUR_WEEKEND);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourWeekendIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourWeekend in DEFAULT_FRAIS_JOUR_WEEKEND or UPDATED_FRAIS_JOUR_WEEKEND
        defaultMissionShouldBeFound("fraisJourWeekend.in=" + DEFAULT_FRAIS_JOUR_WEEKEND + "," + UPDATED_FRAIS_JOUR_WEEKEND);

        // Get all the missionList where fraisJourWeekend equals to UPDATED_FRAIS_JOUR_WEEKEND
        defaultMissionShouldNotBeFound("fraisJourWeekend.in=" + UPDATED_FRAIS_JOUR_WEEKEND);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourWeekendIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourWeekend is not null
        defaultMissionShouldBeFound("fraisJourWeekend.specified=true");

        // Get all the missionList where fraisJourWeekend is null
        defaultMissionShouldNotBeFound("fraisJourWeekend.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourWeekendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourWeekend is greater than or equal to DEFAULT_FRAIS_JOUR_WEEKEND
        defaultMissionShouldBeFound("fraisJourWeekend.greaterThanOrEqual=" + DEFAULT_FRAIS_JOUR_WEEKEND);

        // Get all the missionList where fraisJourWeekend is greater than or equal to UPDATED_FRAIS_JOUR_WEEKEND
        defaultMissionShouldNotBeFound("fraisJourWeekend.greaterThanOrEqual=" + UPDATED_FRAIS_JOUR_WEEKEND);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourWeekendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourWeekend is less than or equal to DEFAULT_FRAIS_JOUR_WEEKEND
        defaultMissionShouldBeFound("fraisJourWeekend.lessThanOrEqual=" + DEFAULT_FRAIS_JOUR_WEEKEND);

        // Get all the missionList where fraisJourWeekend is less than or equal to SMALLER_FRAIS_JOUR_WEEKEND
        defaultMissionShouldNotBeFound("fraisJourWeekend.lessThanOrEqual=" + SMALLER_FRAIS_JOUR_WEEKEND);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourWeekendIsLessThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourWeekend is less than DEFAULT_FRAIS_JOUR_WEEKEND
        defaultMissionShouldNotBeFound("fraisJourWeekend.lessThan=" + DEFAULT_FRAIS_JOUR_WEEKEND);

        // Get all the missionList where fraisJourWeekend is less than UPDATED_FRAIS_JOUR_WEEKEND
        defaultMissionShouldBeFound("fraisJourWeekend.lessThan=" + UPDATED_FRAIS_JOUR_WEEKEND);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisJourWeekendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisJourWeekend is greater than DEFAULT_FRAIS_JOUR_WEEKEND
        defaultMissionShouldNotBeFound("fraisJourWeekend.greaterThan=" + DEFAULT_FRAIS_JOUR_WEEKEND);

        // Get all the missionList where fraisJourWeekend is greater than SMALLER_FRAIS_JOUR_WEEKEND
        defaultMissionShouldBeFound("fraisJourWeekend.greaterThan=" + SMALLER_FRAIS_JOUR_WEEKEND);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisPersonnelIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisPersonnel equals to DEFAULT_FRAIS_PERSONNEL
        defaultMissionShouldBeFound("fraisPersonnel.equals=" + DEFAULT_FRAIS_PERSONNEL);

        // Get all the missionList where fraisPersonnel equals to UPDATED_FRAIS_PERSONNEL
        defaultMissionShouldNotBeFound("fraisPersonnel.equals=" + UPDATED_FRAIS_PERSONNEL);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisPersonnelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisPersonnel not equals to DEFAULT_FRAIS_PERSONNEL
        defaultMissionShouldNotBeFound("fraisPersonnel.notEquals=" + DEFAULT_FRAIS_PERSONNEL);

        // Get all the missionList where fraisPersonnel not equals to UPDATED_FRAIS_PERSONNEL
        defaultMissionShouldBeFound("fraisPersonnel.notEquals=" + UPDATED_FRAIS_PERSONNEL);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisPersonnelIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisPersonnel in DEFAULT_FRAIS_PERSONNEL or UPDATED_FRAIS_PERSONNEL
        defaultMissionShouldBeFound("fraisPersonnel.in=" + DEFAULT_FRAIS_PERSONNEL + "," + UPDATED_FRAIS_PERSONNEL);

        // Get all the missionList where fraisPersonnel equals to UPDATED_FRAIS_PERSONNEL
        defaultMissionShouldNotBeFound("fraisPersonnel.in=" + UPDATED_FRAIS_PERSONNEL);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisPersonnelIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisPersonnel is not null
        defaultMissionShouldBeFound("fraisPersonnel.specified=true");

        // Get all the missionList where fraisPersonnel is null
        defaultMissionShouldNotBeFound("fraisPersonnel.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByFraisPersonnelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisPersonnel is greater than or equal to DEFAULT_FRAIS_PERSONNEL
        defaultMissionShouldBeFound("fraisPersonnel.greaterThanOrEqual=" + DEFAULT_FRAIS_PERSONNEL);

        // Get all the missionList where fraisPersonnel is greater than or equal to UPDATED_FRAIS_PERSONNEL
        defaultMissionShouldNotBeFound("fraisPersonnel.greaterThanOrEqual=" + UPDATED_FRAIS_PERSONNEL);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisPersonnelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisPersonnel is less than or equal to DEFAULT_FRAIS_PERSONNEL
        defaultMissionShouldBeFound("fraisPersonnel.lessThanOrEqual=" + DEFAULT_FRAIS_PERSONNEL);

        // Get all the missionList where fraisPersonnel is less than or equal to SMALLER_FRAIS_PERSONNEL
        defaultMissionShouldNotBeFound("fraisPersonnel.lessThanOrEqual=" + SMALLER_FRAIS_PERSONNEL);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisPersonnelIsLessThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisPersonnel is less than DEFAULT_FRAIS_PERSONNEL
        defaultMissionShouldNotBeFound("fraisPersonnel.lessThan=" + DEFAULT_FRAIS_PERSONNEL);

        // Get all the missionList where fraisPersonnel is less than UPDATED_FRAIS_PERSONNEL
        defaultMissionShouldBeFound("fraisPersonnel.lessThan=" + UPDATED_FRAIS_PERSONNEL);
    }

    @Test
    @Transactional
    void getAllMissionsByFraisPersonnelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where fraisPersonnel is greater than DEFAULT_FRAIS_PERSONNEL
        defaultMissionShouldNotBeFound("fraisPersonnel.greaterThan=" + DEFAULT_FRAIS_PERSONNEL);

        // Get all the missionList where fraisPersonnel is greater than SMALLER_FRAIS_PERSONNEL
        defaultMissionShouldBeFound("fraisPersonnel.greaterThan=" + SMALLER_FRAIS_PERSONNEL);
    }

    @Test
    @Transactional
    void getAllMissionsByMontantTotaleIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where montantTotale equals to DEFAULT_MONTANT_TOTALE
        defaultMissionShouldBeFound("montantTotale.equals=" + DEFAULT_MONTANT_TOTALE);

        // Get all the missionList where montantTotale equals to UPDATED_MONTANT_TOTALE
        defaultMissionShouldNotBeFound("montantTotale.equals=" + UPDATED_MONTANT_TOTALE);
    }

    @Test
    @Transactional
    void getAllMissionsByMontantTotaleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where montantTotale not equals to DEFAULT_MONTANT_TOTALE
        defaultMissionShouldNotBeFound("montantTotale.notEquals=" + DEFAULT_MONTANT_TOTALE);

        // Get all the missionList where montantTotale not equals to UPDATED_MONTANT_TOTALE
        defaultMissionShouldBeFound("montantTotale.notEquals=" + UPDATED_MONTANT_TOTALE);
    }

    @Test
    @Transactional
    void getAllMissionsByMontantTotaleIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where montantTotale in DEFAULT_MONTANT_TOTALE or UPDATED_MONTANT_TOTALE
        defaultMissionShouldBeFound("montantTotale.in=" + DEFAULT_MONTANT_TOTALE + "," + UPDATED_MONTANT_TOTALE);

        // Get all the missionList where montantTotale equals to UPDATED_MONTANT_TOTALE
        defaultMissionShouldNotBeFound("montantTotale.in=" + UPDATED_MONTANT_TOTALE);
    }

    @Test
    @Transactional
    void getAllMissionsByMontantTotaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where montantTotale is not null
        defaultMissionShouldBeFound("montantTotale.specified=true");

        // Get all the missionList where montantTotale is null
        defaultMissionShouldNotBeFound("montantTotale.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByMontantTotaleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where montantTotale is greater than or equal to DEFAULT_MONTANT_TOTALE
        defaultMissionShouldBeFound("montantTotale.greaterThanOrEqual=" + DEFAULT_MONTANT_TOTALE);

        // Get all the missionList where montantTotale is greater than or equal to UPDATED_MONTANT_TOTALE
        defaultMissionShouldNotBeFound("montantTotale.greaterThanOrEqual=" + UPDATED_MONTANT_TOTALE);
    }

    @Test
    @Transactional
    void getAllMissionsByMontantTotaleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where montantTotale is less than or equal to DEFAULT_MONTANT_TOTALE
        defaultMissionShouldBeFound("montantTotale.lessThanOrEqual=" + DEFAULT_MONTANT_TOTALE);

        // Get all the missionList where montantTotale is less than or equal to SMALLER_MONTANT_TOTALE
        defaultMissionShouldNotBeFound("montantTotale.lessThanOrEqual=" + SMALLER_MONTANT_TOTALE);
    }

    @Test
    @Transactional
    void getAllMissionsByMontantTotaleIsLessThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where montantTotale is less than DEFAULT_MONTANT_TOTALE
        defaultMissionShouldNotBeFound("montantTotale.lessThan=" + DEFAULT_MONTANT_TOTALE);

        // Get all the missionList where montantTotale is less than UPDATED_MONTANT_TOTALE
        defaultMissionShouldBeFound("montantTotale.lessThan=" + UPDATED_MONTANT_TOTALE);
    }

    @Test
    @Transactional
    void getAllMissionsByMontantTotaleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where montantTotale is greater than DEFAULT_MONTANT_TOTALE
        defaultMissionShouldNotBeFound("montantTotale.greaterThan=" + DEFAULT_MONTANT_TOTALE);

        // Get all the missionList where montantTotale is greater than SMALLER_MONTANT_TOTALE
        defaultMissionShouldBeFound("montantTotale.greaterThan=" + SMALLER_MONTANT_TOTALE);
    }

    @Test
    @Transactional
    void getAllMissionsByWorkorderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where workorderId equals to DEFAULT_WORKORDER_ID
        defaultMissionShouldBeFound("workorderId.equals=" + DEFAULT_WORKORDER_ID);

        // Get all the missionList where workorderId equals to UPDATED_WORKORDER_ID
        defaultMissionShouldNotBeFound("workorderId.equals=" + UPDATED_WORKORDER_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByWorkorderIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where workorderId not equals to DEFAULT_WORKORDER_ID
        defaultMissionShouldNotBeFound("workorderId.notEquals=" + DEFAULT_WORKORDER_ID);

        // Get all the missionList where workorderId not equals to UPDATED_WORKORDER_ID
        defaultMissionShouldBeFound("workorderId.notEquals=" + UPDATED_WORKORDER_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByWorkorderIdIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where workorderId in DEFAULT_WORKORDER_ID or UPDATED_WORKORDER_ID
        defaultMissionShouldBeFound("workorderId.in=" + DEFAULT_WORKORDER_ID + "," + UPDATED_WORKORDER_ID);

        // Get all the missionList where workorderId equals to UPDATED_WORKORDER_ID
        defaultMissionShouldNotBeFound("workorderId.in=" + UPDATED_WORKORDER_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByWorkorderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where workorderId is not null
        defaultMissionShouldBeFound("workorderId.specified=true");

        // Get all the missionList where workorderId is null
        defaultMissionShouldNotBeFound("workorderId.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByWorkorderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where workorderId is greater than or equal to DEFAULT_WORKORDER_ID
        defaultMissionShouldBeFound("workorderId.greaterThanOrEqual=" + DEFAULT_WORKORDER_ID);

        // Get all the missionList where workorderId is greater than or equal to UPDATED_WORKORDER_ID
        defaultMissionShouldNotBeFound("workorderId.greaterThanOrEqual=" + UPDATED_WORKORDER_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByWorkorderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where workorderId is less than or equal to DEFAULT_WORKORDER_ID
        defaultMissionShouldBeFound("workorderId.lessThanOrEqual=" + DEFAULT_WORKORDER_ID);

        // Get all the missionList where workorderId is less than or equal to SMALLER_WORKORDER_ID
        defaultMissionShouldNotBeFound("workorderId.lessThanOrEqual=" + SMALLER_WORKORDER_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByWorkorderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where workorderId is less than DEFAULT_WORKORDER_ID
        defaultMissionShouldNotBeFound("workorderId.lessThan=" + DEFAULT_WORKORDER_ID);

        // Get all the missionList where workorderId is less than UPDATED_WORKORDER_ID
        defaultMissionShouldBeFound("workorderId.lessThan=" + UPDATED_WORKORDER_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByWorkorderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where workorderId is greater than DEFAULT_WORKORDER_ID
        defaultMissionShouldNotBeFound("workorderId.greaterThan=" + DEFAULT_WORKORDER_ID);

        // Get all the missionList where workorderId is greater than SMALLER_WORKORDER_ID
        defaultMissionShouldBeFound("workorderId.greaterThan=" + SMALLER_WORKORDER_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteFraisIdIsEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteFraisId equals to DEFAULT_NOTE_FRAIS_ID
        defaultMissionShouldBeFound("noteFraisId.equals=" + DEFAULT_NOTE_FRAIS_ID);

        // Get all the missionList where noteFraisId equals to UPDATED_NOTE_FRAIS_ID
        defaultMissionShouldNotBeFound("noteFraisId.equals=" + UPDATED_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteFraisIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteFraisId not equals to DEFAULT_NOTE_FRAIS_ID
        defaultMissionShouldNotBeFound("noteFraisId.notEquals=" + DEFAULT_NOTE_FRAIS_ID);

        // Get all the missionList where noteFraisId not equals to UPDATED_NOTE_FRAIS_ID
        defaultMissionShouldBeFound("noteFraisId.notEquals=" + UPDATED_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteFraisIdIsInShouldWork() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteFraisId in DEFAULT_NOTE_FRAIS_ID or UPDATED_NOTE_FRAIS_ID
        defaultMissionShouldBeFound("noteFraisId.in=" + DEFAULT_NOTE_FRAIS_ID + "," + UPDATED_NOTE_FRAIS_ID);

        // Get all the missionList where noteFraisId equals to UPDATED_NOTE_FRAIS_ID
        defaultMissionShouldNotBeFound("noteFraisId.in=" + UPDATED_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteFraisIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteFraisId is not null
        defaultMissionShouldBeFound("noteFraisId.specified=true");

        // Get all the missionList where noteFraisId is null
        defaultMissionShouldNotBeFound("noteFraisId.specified=false");
    }

    @Test
    @Transactional
    void getAllMissionsByNoteFraisIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteFraisId is greater than or equal to DEFAULT_NOTE_FRAIS_ID
        defaultMissionShouldBeFound("noteFraisId.greaterThanOrEqual=" + DEFAULT_NOTE_FRAIS_ID);

        // Get all the missionList where noteFraisId is greater than or equal to UPDATED_NOTE_FRAIS_ID
        defaultMissionShouldNotBeFound("noteFraisId.greaterThanOrEqual=" + UPDATED_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteFraisIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteFraisId is less than or equal to DEFAULT_NOTE_FRAIS_ID
        defaultMissionShouldBeFound("noteFraisId.lessThanOrEqual=" + DEFAULT_NOTE_FRAIS_ID);

        // Get all the missionList where noteFraisId is less than or equal to SMALLER_NOTE_FRAIS_ID
        defaultMissionShouldNotBeFound("noteFraisId.lessThanOrEqual=" + SMALLER_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteFraisIdIsLessThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteFraisId is less than DEFAULT_NOTE_FRAIS_ID
        defaultMissionShouldNotBeFound("noteFraisId.lessThan=" + DEFAULT_NOTE_FRAIS_ID);

        // Get all the missionList where noteFraisId is less than UPDATED_NOTE_FRAIS_ID
        defaultMissionShouldBeFound("noteFraisId.lessThan=" + UPDATED_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void getAllMissionsByNoteFraisIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList where noteFraisId is greater than DEFAULT_NOTE_FRAIS_ID
        defaultMissionShouldNotBeFound("noteFraisId.greaterThan=" + DEFAULT_NOTE_FRAIS_ID);

        // Get all the missionList where noteFraisId is greater than SMALLER_NOTE_FRAIS_ID
        defaultMissionShouldBeFound("noteFraisId.greaterThan=" + SMALLER_NOTE_FRAIS_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMissionShouldBeFound(String filter) throws Exception {
        restMissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mission.getId().intValue())))
            .andExpect(jsonPath("$.[*].titreBeneficiaire").value(hasItem(DEFAULT_TITRE_BENEFICIAIRE)))
            .andExpect(jsonPath("$.[*].noteMission").value(hasItem(sameNumber(DEFAULT_NOTE_MISSION))))
            .andExpect(jsonPath("$.[*].contactSocieteId").value(hasItem(DEFAULT_CONTACT_SOCIETE_ID.intValue())))
            .andExpect(jsonPath("$.[*].contactSocieteMatricule").value(hasItem(DEFAULT_CONTACT_SOCIETE_MATRICULE)))
            .andExpect(jsonPath("$.[*].fraisJourFerie").value(hasItem(sameNumber(DEFAULT_FRAIS_JOUR_FERIE))))
            .andExpect(jsonPath("$.[*].fraisJourWeekend").value(hasItem(sameNumber(DEFAULT_FRAIS_JOUR_WEEKEND))))
            .andExpect(jsonPath("$.[*].fraisPersonnel").value(hasItem(sameNumber(DEFAULT_FRAIS_PERSONNEL))))
            .andExpect(jsonPath("$.[*].montantTotale").value(hasItem(sameNumber(DEFAULT_MONTANT_TOTALE))))
            .andExpect(jsonPath("$.[*].workorderId").value(hasItem(DEFAULT_WORKORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].noteFraisId").value(hasItem(DEFAULT_NOTE_FRAIS_ID.intValue())));

        // Check, that the count call also returns 1
        restMissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMissionShouldNotBeFound(String filter) throws Exception {
        restMissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMission() throws Exception {
        // Get the mission
        restMissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Update the mission
        Mission updatedMission = missionRepository.findById(mission.getId()).get();
        // Disconnect from session so that the updates on updatedMission are not directly saved in db
        em.detach(updatedMission);
        updatedMission
            .titreBeneficiaire(UPDATED_TITRE_BENEFICIAIRE)
            .noteMission(UPDATED_NOTE_MISSION)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID)
            .contactSocieteMatricule(UPDATED_CONTACT_SOCIETE_MATRICULE)
            .fraisJourFerie(UPDATED_FRAIS_JOUR_FERIE)
            .fraisJourWeekend(UPDATED_FRAIS_JOUR_WEEKEND)
            .fraisPersonnel(UPDATED_FRAIS_PERSONNEL)
            .montantTotale(UPDATED_MONTANT_TOTALE)
            .workorderId(UPDATED_WORKORDER_ID)
            .noteFraisId(UPDATED_NOTE_FRAIS_ID);
        MissionDTO missionDTO = missionMapper.toDto(updatedMission);

        restMissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, missionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
        Mission testMission = missionList.get(missionList.size() - 1);
        assertThat(testMission.getTitreBeneficiaire()).isEqualTo(UPDATED_TITRE_BENEFICIAIRE);
        assertThat(testMission.getNoteMission()).isEqualTo(UPDATED_NOTE_MISSION);
        assertThat(testMission.getContactSocieteId()).isEqualTo(UPDATED_CONTACT_SOCIETE_ID);
        assertThat(testMission.getContactSocieteMatricule()).isEqualTo(UPDATED_CONTACT_SOCIETE_MATRICULE);
        assertThat(testMission.getFraisJourFerie()).isEqualTo(UPDATED_FRAIS_JOUR_FERIE);
        assertThat(testMission.getFraisJourWeekend()).isEqualTo(UPDATED_FRAIS_JOUR_WEEKEND);
        assertThat(testMission.getFraisPersonnel()).isEqualTo(UPDATED_FRAIS_PERSONNEL);
        assertThat(testMission.getMontantTotale()).isEqualTo(UPDATED_MONTANT_TOTALE);
        assertThat(testMission.getWorkorderId()).isEqualTo(UPDATED_WORKORDER_ID);
        assertThat(testMission.getNoteFraisId()).isEqualTo(UPDATED_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void putNonExistingMission() throws Exception {
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();
        mission.setId(count.incrementAndGet());

        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, missionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMission() throws Exception {
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();
        mission.setId(count.incrementAndGet());

        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMission() throws Exception {
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();
        mission.setId(count.incrementAndGet());

        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMissionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMissionWithPatch() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Update the mission using partial update
        Mission partialUpdatedMission = new Mission();
        partialUpdatedMission.setId(mission.getId());

        partialUpdatedMission
            .noteMission(UPDATED_NOTE_MISSION)
            .fraisPersonnel(UPDATED_FRAIS_PERSONNEL)
            .montantTotale(UPDATED_MONTANT_TOTALE);

        restMissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMission))
            )
            .andExpect(status().isOk());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
        Mission testMission = missionList.get(missionList.size() - 1);
        assertThat(testMission.getTitreBeneficiaire()).isEqualTo(DEFAULT_TITRE_BENEFICIAIRE);
        assertThat(testMission.getNoteMission()).isEqualByComparingTo(UPDATED_NOTE_MISSION);
        assertThat(testMission.getContactSocieteId()).isEqualTo(DEFAULT_CONTACT_SOCIETE_ID);
        assertThat(testMission.getContactSocieteMatricule()).isEqualTo(DEFAULT_CONTACT_SOCIETE_MATRICULE);
        assertThat(testMission.getFraisJourFerie()).isEqualByComparingTo(DEFAULT_FRAIS_JOUR_FERIE);
        assertThat(testMission.getFraisJourWeekend()).isEqualByComparingTo(DEFAULT_FRAIS_JOUR_WEEKEND);
        assertThat(testMission.getFraisPersonnel()).isEqualByComparingTo(UPDATED_FRAIS_PERSONNEL);
        assertThat(testMission.getMontantTotale()).isEqualByComparingTo(UPDATED_MONTANT_TOTALE);
        assertThat(testMission.getWorkorderId()).isEqualTo(DEFAULT_WORKORDER_ID);
        assertThat(testMission.getNoteFraisId()).isEqualTo(DEFAULT_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void fullUpdateMissionWithPatch() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Update the mission using partial update
        Mission partialUpdatedMission = new Mission();
        partialUpdatedMission.setId(mission.getId());

        partialUpdatedMission
            .titreBeneficiaire(UPDATED_TITRE_BENEFICIAIRE)
            .noteMission(UPDATED_NOTE_MISSION)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID)
            .contactSocieteMatricule(UPDATED_CONTACT_SOCIETE_MATRICULE)
            .fraisJourFerie(UPDATED_FRAIS_JOUR_FERIE)
            .fraisJourWeekend(UPDATED_FRAIS_JOUR_WEEKEND)
            .fraisPersonnel(UPDATED_FRAIS_PERSONNEL)
            .montantTotale(UPDATED_MONTANT_TOTALE)
            .workorderId(UPDATED_WORKORDER_ID)
            .noteFraisId(UPDATED_NOTE_FRAIS_ID);

        restMissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMission))
            )
            .andExpect(status().isOk());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
        Mission testMission = missionList.get(missionList.size() - 1);
        assertThat(testMission.getTitreBeneficiaire()).isEqualTo(UPDATED_TITRE_BENEFICIAIRE);
        assertThat(testMission.getNoteMission()).isEqualByComparingTo(UPDATED_NOTE_MISSION);
        assertThat(testMission.getContactSocieteId()).isEqualTo(UPDATED_CONTACT_SOCIETE_ID);
        assertThat(testMission.getContactSocieteMatricule()).isEqualTo(UPDATED_CONTACT_SOCIETE_MATRICULE);
        assertThat(testMission.getFraisJourFerie()).isEqualByComparingTo(UPDATED_FRAIS_JOUR_FERIE);
        assertThat(testMission.getFraisJourWeekend()).isEqualByComparingTo(UPDATED_FRAIS_JOUR_WEEKEND);
        assertThat(testMission.getFraisPersonnel()).isEqualByComparingTo(UPDATED_FRAIS_PERSONNEL);
        assertThat(testMission.getMontantTotale()).isEqualByComparingTo(UPDATED_MONTANT_TOTALE);
        assertThat(testMission.getWorkorderId()).isEqualTo(UPDATED_WORKORDER_ID);
        assertThat(testMission.getNoteFraisId()).isEqualTo(UPDATED_NOTE_FRAIS_ID);
    }

    @Test
    @Transactional
    void patchNonExistingMission() throws Exception {
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();
        mission.setId(count.incrementAndGet());

        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, missionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMission() throws Exception {
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();
        mission.setId(count.incrementAndGet());

        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMission() throws Exception {
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();
        mission.setId(count.incrementAndGet());

        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(missionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        int databaseSizeBeforeDelete = missionRepository.findAll().size();

        // Delete the mission
        restMissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, mission.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
