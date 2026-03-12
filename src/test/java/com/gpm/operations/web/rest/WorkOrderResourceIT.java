package com.gpm.operations.web.rest;

import static com.gpm.operations.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.service.dto.WorkOrderDTO;
import com.gpm.operations.service.mapper.WorkOrderMapper;
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
 * Integration tests for the {@link WorkOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkOrderResourceIT {

    private static final Long DEFAULT_CLIENT_ID = 1L;
    private static final Long UPDATED_CLIENT_ID = 2L;

    private static final Long DEFAULT_AFFAIRE_ID = 1L;
    private static final Long UPDATED_AFFAIRE_ID = 2L;

    private static final Long DEFAULT_DEMANDEUR_CONTACT_ID = 1L;
    private static final Long UPDATED_DEMANDEUR_CONTACT_ID = 2L;

    private static final Long DEFAULT_VEHICULE_ID = 1L;
    private static final Long UPDATED_VEHICULE_ID = 2L;

    private static final Long DEFAULT_OT_EXTERNE_ID = 1L;
    private static final Long UPDATED_OT_EXTERNE_ID = 2L;

    private static final String DEFAULT_VALIDEUR_ID = "AAAAAAAAAA";
    private static final String UPDATED_VALIDEUR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_VALIDEUR_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_VALIDEUR_USER_LOGIN = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_HEURE_DEBUT_PREV = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_HEURE_DEBUT_PREV = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_HEURE_FIN_PREV = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_HEURE_FIN_PREV = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_HEURE_DEBUT_REEL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_HEURE_DEBUT_REEL = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_HEURE_FIN_REEL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_HEURE_FIN_REEL = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_MISSION_DE_NUIT = false;
    private static final Boolean UPDATED_MISSION_DE_NUIT = true;

    private static final Integer DEFAULT_NOMBRE_NUITS = 1;
    private static final Integer UPDATED_NOMBRE_NUITS = 2;

    private static final Boolean DEFAULT_HEBERGEMENT = false;
    private static final Boolean UPDATED_HEBERGEMENT = true;

    private static final Integer DEFAULT_NOMBRE_HEBERGEMENTS = 1;
    private static final Integer UPDATED_NOMBRE_HEBERGEMENTS = 2;

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_FICHE_INTERVENTION = "AAAAAAAAAA";
    private static final String UPDATED_NUM_FICHE_INTERVENTION = "BBBBBBBBBB";

    private static final StatutWO DEFAULT_STATUT = StatutWO.Creation;
    private static final StatutWO UPDATED_STATUT = StatutWO.ExecutionTravaux;

    private static final String DEFAULT_MATERIEL_UTILISE = "AAAAAAAAAA";
    private static final String UPDATED_MATERIEL_UTILISE = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/work-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkOrderMockMvc;

    private WorkOrder workOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrder createEntity(EntityManager em) {
        WorkOrder workOrder = new WorkOrder()
            .clientId(DEFAULT_CLIENT_ID)
            .affaireId(DEFAULT_AFFAIRE_ID)
            .demandeurContactId(DEFAULT_DEMANDEUR_CONTACT_ID)
            .vehiculeId(DEFAULT_VEHICULE_ID)
            .otExterneId(DEFAULT_OT_EXTERNE_ID)
            .valideurId(DEFAULT_VALIDEUR_ID)
            .valideurUserLogin(DEFAULT_VALIDEUR_USER_LOGIN)
            .dateHeureDebutPrev(DEFAULT_DATE_HEURE_DEBUT_PREV)
            .dateHeureFinPrev(DEFAULT_DATE_HEURE_FIN_PREV)
            .dateHeureDebutReel(DEFAULT_DATE_HEURE_DEBUT_REEL)
            .dateHeureFinReel(DEFAULT_DATE_HEURE_FIN_REEL)
            .missionDeNuit(DEFAULT_MISSION_DE_NUIT)
            .nombreNuits(DEFAULT_NOMBRE_NUITS)
            .hebergement(DEFAULT_HEBERGEMENT)
            .nombreHebergements(DEFAULT_NOMBRE_HEBERGEMENTS)
            .remarque(DEFAULT_REMARQUE)
            .numFicheIntervention(DEFAULT_NUM_FICHE_INTERVENTION)
            .statut(DEFAULT_STATUT)
            .materielUtilise(DEFAULT_MATERIEL_UTILISE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdByUserLogin(DEFAULT_CREATED_BY_USER_LOGIN)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedByUserLogin(DEFAULT_UPDATED_BY_USER_LOGIN);
        return workOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrder createUpdatedEntity(EntityManager em) {
        WorkOrder workOrder = new WorkOrder()
            .clientId(UPDATED_CLIENT_ID)
            .affaireId(UPDATED_AFFAIRE_ID)
            .demandeurContactId(UPDATED_DEMANDEUR_CONTACT_ID)
            .vehiculeId(UPDATED_VEHICULE_ID)
            .otExterneId(UPDATED_OT_EXTERNE_ID)
            .valideurId(UPDATED_VALIDEUR_ID)
            .valideurUserLogin(UPDATED_VALIDEUR_USER_LOGIN)
            .dateHeureDebutPrev(UPDATED_DATE_HEURE_DEBUT_PREV)
            .dateHeureFinPrev(UPDATED_DATE_HEURE_FIN_PREV)
            .dateHeureDebutReel(UPDATED_DATE_HEURE_DEBUT_REEL)
            .dateHeureFinReel(UPDATED_DATE_HEURE_FIN_REEL)
            .missionDeNuit(UPDATED_MISSION_DE_NUIT)
            .nombreNuits(UPDATED_NOMBRE_NUITS)
            .hebergement(UPDATED_HEBERGEMENT)
            .nombreHebergements(UPDATED_NOMBRE_HEBERGEMENTS)
            .remarque(UPDATED_REMARQUE)
            .numFicheIntervention(UPDATED_NUM_FICHE_INTERVENTION)
            .statut(UPDATED_STATUT)
            .materielUtilise(UPDATED_MATERIEL_UTILISE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        return workOrder;
    }

    @BeforeEach
    public void initTest() {
        workOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkOrder() throws Exception {
        int databaseSizeBeforeCreate = workOrderRepository.findAll().size();
        // Create the WorkOrder
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);
        restWorkOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrder testWorkOrder = workOrderList.get(workOrderList.size() - 1);
        assertThat(testWorkOrder.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testWorkOrder.getAffaireId()).isEqualTo(DEFAULT_AFFAIRE_ID);
        assertThat(testWorkOrder.getDemandeurContactId()).isEqualTo(DEFAULT_DEMANDEUR_CONTACT_ID);
        assertThat(testWorkOrder.getVehiculeId()).isEqualTo(DEFAULT_VEHICULE_ID);
        assertThat(testWorkOrder.getOtExterneId()).isEqualTo(DEFAULT_OT_EXTERNE_ID);
        assertThat(testWorkOrder.getValideurId()).isEqualTo(DEFAULT_VALIDEUR_ID);
        assertThat(testWorkOrder.getValideurUserLogin()).isEqualTo(DEFAULT_VALIDEUR_USER_LOGIN);
        assertThat(testWorkOrder.getDateHeureDebutPrev()).isEqualTo(DEFAULT_DATE_HEURE_DEBUT_PREV);
        assertThat(testWorkOrder.getDateHeureFinPrev()).isEqualTo(DEFAULT_DATE_HEURE_FIN_PREV);
        assertThat(testWorkOrder.getDateHeureDebutReel()).isEqualTo(DEFAULT_DATE_HEURE_DEBUT_REEL);
        assertThat(testWorkOrder.getDateHeureFinReel()).isEqualTo(DEFAULT_DATE_HEURE_FIN_REEL);
        assertThat(testWorkOrder.getMissionDeNuit()).isEqualTo(DEFAULT_MISSION_DE_NUIT);
        assertThat(testWorkOrder.getNombreNuits()).isEqualTo(DEFAULT_NOMBRE_NUITS);
        assertThat(testWorkOrder.getHebergement()).isEqualTo(DEFAULT_HEBERGEMENT);
        assertThat(testWorkOrder.getNombreHebergements()).isEqualTo(DEFAULT_NOMBRE_HEBERGEMENTS);
        assertThat(testWorkOrder.getRemarque()).isEqualTo(DEFAULT_REMARQUE);
        assertThat(testWorkOrder.getNumFicheIntervention()).isEqualTo(DEFAULT_NUM_FICHE_INTERVENTION);
        assertThat(testWorkOrder.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testWorkOrder.getMaterielUtilise()).isEqualTo(DEFAULT_MATERIEL_UTILISE);
        assertThat(testWorkOrder.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testWorkOrder.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testWorkOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testWorkOrder.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testWorkOrder.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testWorkOrder.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void createWorkOrderWithExistingId() throws Exception {
        // Create the WorkOrder with an existing ID
        workOrder.setId(1L);
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        int databaseSizeBeforeCreate = workOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateHeureDebutPrevIsRequired() throws Exception {
        int databaseSizeBeforeTest = workOrderRepository.findAll().size();
        // set the field null
        workOrder.setDateHeureDebutPrev(null);

        // Create the WorkOrder, which fails.
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        restWorkOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateHeureFinPrevIsRequired() throws Exception {
        int databaseSizeBeforeTest = workOrderRepository.findAll().size();
        // set the field null
        workOrder.setDateHeureFinPrev(null);

        // Create the WorkOrder, which fails.
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        restWorkOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMissionDeNuitIsRequired() throws Exception {
        int databaseSizeBeforeTest = workOrderRepository.findAll().size();
        // set the field null
        workOrder.setMissionDeNuit(null);

        // Create the WorkOrder, which fails.
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        restWorkOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHebergementIsRequired() throws Exception {
        int databaseSizeBeforeTest = workOrderRepository.findAll().size();
        // set the field null
        workOrder.setHebergement(null);

        // Create the WorkOrder, which fails.
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        restWorkOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = workOrderRepository.findAll().size();
        // set the field null
        workOrder.setStatut(null);

        // Create the WorkOrder, which fails.
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        restWorkOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkOrders() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        // Get all the workOrderList
        restWorkOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].affaireId").value(hasItem(DEFAULT_AFFAIRE_ID.intValue())))
            .andExpect(jsonPath("$.[*].demandeurContactId").value(hasItem(DEFAULT_DEMANDEUR_CONTACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].vehiculeId").value(hasItem(DEFAULT_VEHICULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].otExterneId").value(hasItem(DEFAULT_OT_EXTERNE_ID.intValue())))
            .andExpect(jsonPath("$.[*].valideurId").value(hasItem(DEFAULT_VALIDEUR_ID)))
            .andExpect(jsonPath("$.[*].valideurUserLogin").value(hasItem(DEFAULT_VALIDEUR_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].dateHeureDebutPrev").value(hasItem(sameInstant(DEFAULT_DATE_HEURE_DEBUT_PREV))))
            .andExpect(jsonPath("$.[*].dateHeureFinPrev").value(hasItem(sameInstant(DEFAULT_DATE_HEURE_FIN_PREV))))
            .andExpect(jsonPath("$.[*].dateHeureDebutReel").value(hasItem(sameInstant(DEFAULT_DATE_HEURE_DEBUT_REEL))))
            .andExpect(jsonPath("$.[*].dateHeureFinReel").value(hasItem(sameInstant(DEFAULT_DATE_HEURE_FIN_REEL))))
            .andExpect(jsonPath("$.[*].missionDeNuit").value(hasItem(DEFAULT_MISSION_DE_NUIT.booleanValue())))
            .andExpect(jsonPath("$.[*].nombreNuits").value(hasItem(DEFAULT_NOMBRE_NUITS)))
            .andExpect(jsonPath("$.[*].hebergement").value(hasItem(DEFAULT_HEBERGEMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].nombreHebergements").value(hasItem(DEFAULT_NOMBRE_HEBERGEMENTS)))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE)))
            .andExpect(jsonPath("$.[*].numFicheIntervention").value(hasItem(DEFAULT_NUM_FICHE_INTERVENTION)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].materielUtilise").value(hasItem(DEFAULT_MATERIEL_UTILISE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdByUserLogin").value(hasItem(DEFAULT_CREATED_BY_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedByUserLogin").value(hasItem(DEFAULT_UPDATED_BY_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getWorkOrder() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        // Get the workOrder
        restWorkOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, workOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workOrder.getId().intValue()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.intValue()))
            .andExpect(jsonPath("$.affaireId").value(DEFAULT_AFFAIRE_ID.intValue()))
            .andExpect(jsonPath("$.demandeurContactId").value(DEFAULT_DEMANDEUR_CONTACT_ID.intValue()))
            .andExpect(jsonPath("$.vehiculeId").value(DEFAULT_VEHICULE_ID.intValue()))
            .andExpect(jsonPath("$.otExterneId").value(DEFAULT_OT_EXTERNE_ID.intValue()))
            .andExpect(jsonPath("$.valideurId").value(DEFAULT_VALIDEUR_ID))
            .andExpect(jsonPath("$.valideurUserLogin").value(DEFAULT_VALIDEUR_USER_LOGIN))
            .andExpect(jsonPath("$.dateHeureDebutPrev").value(sameInstant(DEFAULT_DATE_HEURE_DEBUT_PREV)))
            .andExpect(jsonPath("$.dateHeureFinPrev").value(sameInstant(DEFAULT_DATE_HEURE_FIN_PREV)))
            .andExpect(jsonPath("$.dateHeureDebutReel").value(sameInstant(DEFAULT_DATE_HEURE_DEBUT_REEL)))
            .andExpect(jsonPath("$.dateHeureFinReel").value(sameInstant(DEFAULT_DATE_HEURE_FIN_REEL)))
            .andExpect(jsonPath("$.missionDeNuit").value(DEFAULT_MISSION_DE_NUIT.booleanValue()))
            .andExpect(jsonPath("$.nombreNuits").value(DEFAULT_NOMBRE_NUITS))
            .andExpect(jsonPath("$.hebergement").value(DEFAULT_HEBERGEMENT.booleanValue()))
            .andExpect(jsonPath("$.nombreHebergements").value(DEFAULT_NOMBRE_HEBERGEMENTS))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE))
            .andExpect(jsonPath("$.numFicheIntervention").value(DEFAULT_NUM_FICHE_INTERVENTION))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.materielUtilise").value(DEFAULT_MATERIEL_UTILISE))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdByUserLogin").value(DEFAULT_CREATED_BY_USER_LOGIN))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedByUserLogin").value(DEFAULT_UPDATED_BY_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingWorkOrder() throws Exception {
        // Get the workOrder
        restWorkOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkOrder() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();

        // Update the workOrder
        WorkOrder updatedWorkOrder = workOrderRepository.findById(workOrder.getId()).get();
        // Disconnect from session so that the updates on updatedWorkOrder are not directly saved in db
        em.detach(updatedWorkOrder);
        updatedWorkOrder
            .clientId(UPDATED_CLIENT_ID)
            .affaireId(UPDATED_AFFAIRE_ID)
            .demandeurContactId(UPDATED_DEMANDEUR_CONTACT_ID)
            .vehiculeId(UPDATED_VEHICULE_ID)
            .otExterneId(UPDATED_OT_EXTERNE_ID)
            .valideurId(UPDATED_VALIDEUR_ID)
            .valideurUserLogin(UPDATED_VALIDEUR_USER_LOGIN)
            .dateHeureDebutPrev(UPDATED_DATE_HEURE_DEBUT_PREV)
            .dateHeureFinPrev(UPDATED_DATE_HEURE_FIN_PREV)
            .dateHeureDebutReel(UPDATED_DATE_HEURE_DEBUT_REEL)
            .dateHeureFinReel(UPDATED_DATE_HEURE_FIN_REEL)
            .missionDeNuit(UPDATED_MISSION_DE_NUIT)
            .nombreNuits(UPDATED_NOMBRE_NUITS)
            .hebergement(UPDATED_HEBERGEMENT)
            .nombreHebergements(UPDATED_NOMBRE_HEBERGEMENTS)
            .remarque(UPDATED_REMARQUE)
            .numFicheIntervention(UPDATED_NUM_FICHE_INTERVENTION)
            .statut(UPDATED_STATUT)
            .materielUtilise(UPDATED_MATERIEL_UTILISE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(updatedWorkOrder);

        restWorkOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
        WorkOrder testWorkOrder = workOrderList.get(workOrderList.size() - 1);
        assertThat(testWorkOrder.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testWorkOrder.getAffaireId()).isEqualTo(UPDATED_AFFAIRE_ID);
        assertThat(testWorkOrder.getDemandeurContactId()).isEqualTo(UPDATED_DEMANDEUR_CONTACT_ID);
        assertThat(testWorkOrder.getVehiculeId()).isEqualTo(UPDATED_VEHICULE_ID);
        assertThat(testWorkOrder.getOtExterneId()).isEqualTo(UPDATED_OT_EXTERNE_ID);
        assertThat(testWorkOrder.getValideurId()).isEqualTo(UPDATED_VALIDEUR_ID);
        assertThat(testWorkOrder.getValideurUserLogin()).isEqualTo(UPDATED_VALIDEUR_USER_LOGIN);
        assertThat(testWorkOrder.getDateHeureDebutPrev()).isEqualTo(UPDATED_DATE_HEURE_DEBUT_PREV);
        assertThat(testWorkOrder.getDateHeureFinPrev()).isEqualTo(UPDATED_DATE_HEURE_FIN_PREV);
        assertThat(testWorkOrder.getDateHeureDebutReel()).isEqualTo(UPDATED_DATE_HEURE_DEBUT_REEL);
        assertThat(testWorkOrder.getDateHeureFinReel()).isEqualTo(UPDATED_DATE_HEURE_FIN_REEL);
        assertThat(testWorkOrder.getMissionDeNuit()).isEqualTo(UPDATED_MISSION_DE_NUIT);
        assertThat(testWorkOrder.getNombreNuits()).isEqualTo(UPDATED_NOMBRE_NUITS);
        assertThat(testWorkOrder.getHebergement()).isEqualTo(UPDATED_HEBERGEMENT);
        assertThat(testWorkOrder.getNombreHebergements()).isEqualTo(UPDATED_NOMBRE_HEBERGEMENTS);
        assertThat(testWorkOrder.getRemarque()).isEqualTo(UPDATED_REMARQUE);
        assertThat(testWorkOrder.getNumFicheIntervention()).isEqualTo(UPDATED_NUM_FICHE_INTERVENTION);
        assertThat(testWorkOrder.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testWorkOrder.getMaterielUtilise()).isEqualTo(UPDATED_MATERIEL_UTILISE);
        assertThat(testWorkOrder.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWorkOrder.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testWorkOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWorkOrder.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testWorkOrder.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testWorkOrder.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingWorkOrder() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();
        workOrder.setId(count.incrementAndGet());

        // Create the WorkOrder
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkOrder() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();
        workOrder.setId(count.incrementAndGet());

        // Create the WorkOrder
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkOrder() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();
        workOrder.setId(count.incrementAndGet());

        // Create the WorkOrder
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkOrderWithPatch() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();

        // Update the workOrder using partial update
        WorkOrder partialUpdatedWorkOrder = new WorkOrder();
        partialUpdatedWorkOrder.setId(workOrder.getId());

        partialUpdatedWorkOrder
            .clientId(UPDATED_CLIENT_ID)
            .affaireId(UPDATED_AFFAIRE_ID)
            .demandeurContactId(UPDATED_DEMANDEUR_CONTACT_ID)
            .valideurUserLogin(UPDATED_VALIDEUR_USER_LOGIN)
            .dateHeureFinPrev(UPDATED_DATE_HEURE_FIN_PREV)
            .dateHeureDebutReel(UPDATED_DATE_HEURE_DEBUT_REEL)
            .remarque(UPDATED_REMARQUE)
            .materielUtilise(UPDATED_MATERIEL_UTILISE)
            .createdAt(UPDATED_CREATED_AT)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN);

        restWorkOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrder))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
        WorkOrder testWorkOrder = workOrderList.get(workOrderList.size() - 1);
        assertThat(testWorkOrder.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testWorkOrder.getAffaireId()).isEqualTo(UPDATED_AFFAIRE_ID);
        assertThat(testWorkOrder.getDemandeurContactId()).isEqualTo(UPDATED_DEMANDEUR_CONTACT_ID);
        assertThat(testWorkOrder.getVehiculeId()).isEqualTo(DEFAULT_VEHICULE_ID);
        assertThat(testWorkOrder.getOtExterneId()).isEqualTo(DEFAULT_OT_EXTERNE_ID);
        assertThat(testWorkOrder.getValideurId()).isEqualTo(DEFAULT_VALIDEUR_ID);
        assertThat(testWorkOrder.getValideurUserLogin()).isEqualTo(UPDATED_VALIDEUR_USER_LOGIN);
        assertThat(testWorkOrder.getDateHeureDebutPrev()).isEqualTo(DEFAULT_DATE_HEURE_DEBUT_PREV);
        assertThat(testWorkOrder.getDateHeureFinPrev()).isEqualTo(UPDATED_DATE_HEURE_FIN_PREV);
        assertThat(testWorkOrder.getDateHeureDebutReel()).isEqualTo(UPDATED_DATE_HEURE_DEBUT_REEL);
        assertThat(testWorkOrder.getDateHeureFinReel()).isEqualTo(DEFAULT_DATE_HEURE_FIN_REEL);
        assertThat(testWorkOrder.getMissionDeNuit()).isEqualTo(DEFAULT_MISSION_DE_NUIT);
        assertThat(testWorkOrder.getNombreNuits()).isEqualTo(DEFAULT_NOMBRE_NUITS);
        assertThat(testWorkOrder.getHebergement()).isEqualTo(DEFAULT_HEBERGEMENT);
        assertThat(testWorkOrder.getNombreHebergements()).isEqualTo(DEFAULT_NOMBRE_HEBERGEMENTS);
        assertThat(testWorkOrder.getRemarque()).isEqualTo(UPDATED_REMARQUE);
        assertThat(testWorkOrder.getNumFicheIntervention()).isEqualTo(DEFAULT_NUM_FICHE_INTERVENTION);
        assertThat(testWorkOrder.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testWorkOrder.getMaterielUtilise()).isEqualTo(UPDATED_MATERIEL_UTILISE);
        assertThat(testWorkOrder.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWorkOrder.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testWorkOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testWorkOrder.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testWorkOrder.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testWorkOrder.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateWorkOrderWithPatch() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();

        // Update the workOrder using partial update
        WorkOrder partialUpdatedWorkOrder = new WorkOrder();
        partialUpdatedWorkOrder.setId(workOrder.getId());

        partialUpdatedWorkOrder
            .clientId(UPDATED_CLIENT_ID)
            .affaireId(UPDATED_AFFAIRE_ID)
            .demandeurContactId(UPDATED_DEMANDEUR_CONTACT_ID)
            .vehiculeId(UPDATED_VEHICULE_ID)
            .otExterneId(UPDATED_OT_EXTERNE_ID)
            .valideurId(UPDATED_VALIDEUR_ID)
            .valideurUserLogin(UPDATED_VALIDEUR_USER_LOGIN)
            .dateHeureDebutPrev(UPDATED_DATE_HEURE_DEBUT_PREV)
            .dateHeureFinPrev(UPDATED_DATE_HEURE_FIN_PREV)
            .dateHeureDebutReel(UPDATED_DATE_HEURE_DEBUT_REEL)
            .dateHeureFinReel(UPDATED_DATE_HEURE_FIN_REEL)
            .missionDeNuit(UPDATED_MISSION_DE_NUIT)
            .nombreNuits(UPDATED_NOMBRE_NUITS)
            .hebergement(UPDATED_HEBERGEMENT)
            .nombreHebergements(UPDATED_NOMBRE_HEBERGEMENTS)
            .remarque(UPDATED_REMARQUE)
            .numFicheIntervention(UPDATED_NUM_FICHE_INTERVENTION)
            .statut(UPDATED_STATUT)
            .materielUtilise(UPDATED_MATERIEL_UTILISE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restWorkOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrder))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
        WorkOrder testWorkOrder = workOrderList.get(workOrderList.size() - 1);
        assertThat(testWorkOrder.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testWorkOrder.getAffaireId()).isEqualTo(UPDATED_AFFAIRE_ID);
        assertThat(testWorkOrder.getDemandeurContactId()).isEqualTo(UPDATED_DEMANDEUR_CONTACT_ID);
        assertThat(testWorkOrder.getVehiculeId()).isEqualTo(UPDATED_VEHICULE_ID);
        assertThat(testWorkOrder.getOtExterneId()).isEqualTo(UPDATED_OT_EXTERNE_ID);
        assertThat(testWorkOrder.getValideurId()).isEqualTo(UPDATED_VALIDEUR_ID);
        assertThat(testWorkOrder.getValideurUserLogin()).isEqualTo(UPDATED_VALIDEUR_USER_LOGIN);
        assertThat(testWorkOrder.getDateHeureDebutPrev()).isEqualTo(UPDATED_DATE_HEURE_DEBUT_PREV);
        assertThat(testWorkOrder.getDateHeureFinPrev()).isEqualTo(UPDATED_DATE_HEURE_FIN_PREV);
        assertThat(testWorkOrder.getDateHeureDebutReel()).isEqualTo(UPDATED_DATE_HEURE_DEBUT_REEL);
        assertThat(testWorkOrder.getDateHeureFinReel()).isEqualTo(UPDATED_DATE_HEURE_FIN_REEL);
        assertThat(testWorkOrder.getMissionDeNuit()).isEqualTo(UPDATED_MISSION_DE_NUIT);
        assertThat(testWorkOrder.getNombreNuits()).isEqualTo(UPDATED_NOMBRE_NUITS);
        assertThat(testWorkOrder.getHebergement()).isEqualTo(UPDATED_HEBERGEMENT);
        assertThat(testWorkOrder.getNombreHebergements()).isEqualTo(UPDATED_NOMBRE_HEBERGEMENTS);
        assertThat(testWorkOrder.getRemarque()).isEqualTo(UPDATED_REMARQUE);
        assertThat(testWorkOrder.getNumFicheIntervention()).isEqualTo(UPDATED_NUM_FICHE_INTERVENTION);
        assertThat(testWorkOrder.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testWorkOrder.getMaterielUtilise()).isEqualTo(UPDATED_MATERIEL_UTILISE);
        assertThat(testWorkOrder.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWorkOrder.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testWorkOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWorkOrder.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testWorkOrder.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testWorkOrder.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingWorkOrder() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();
        workOrder.setId(count.incrementAndGet());

        // Create the WorkOrder
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workOrderDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkOrder() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();
        workOrder.setId(count.incrementAndGet());

        // Create the WorkOrder
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkOrder() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();
        workOrder.setId(count.incrementAndGet());

        // Create the WorkOrder
        WorkOrderDTO workOrderDTO = workOrderMapper.toDto(workOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkOrder() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        int databaseSizeBeforeDelete = workOrderRepository.findAll().size();

        // Delete the workOrder
        restWorkOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, workOrder.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
