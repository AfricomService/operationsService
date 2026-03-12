package com.gpm.operations.web.rest;

import static com.gpm.operations.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.SST;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.repository.SSTRepository;
import com.gpm.operations.service.dto.SSTDTO;
import com.gpm.operations.service.mapper.SSTMapper;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link SSTResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SSTResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_IMPORTANCE = "AAAAAAAAAA";
    private static final String UPDATED_IMPORTANCE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INCIDENT_PRESENT = false;
    private static final Boolean UPDATED_INCIDENT_PRESENT = true;

    private static final Boolean DEFAULT_ARRET_TRAVAIL = false;
    private static final Boolean UPDATED_ARRET_TRAVAIL = true;

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

    private static final String ENTITY_API_URL = "/api/ssts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SSTRepository sSTRepository;

    @Autowired
    private SSTMapper sSTMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSSTMockMvc;

    private SST sST;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SST createEntity(EntityManager em) {
        SST sST = new SST()
            .label(DEFAULT_LABEL)
            .date(DEFAULT_DATE)
            .importance(DEFAULT_IMPORTANCE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .incidentPresent(DEFAULT_INCIDENT_PRESENT)
            .arretTravail(DEFAULT_ARRET_TRAVAIL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdByUserLogin(DEFAULT_CREATED_BY_USER_LOGIN)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedByUserLogin(DEFAULT_UPDATED_BY_USER_LOGIN);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        sST.setWorkOrder(workOrder);
        return sST;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SST createUpdatedEntity(EntityManager em) {
        SST sST = new SST()
            .label(UPDATED_LABEL)
            .date(UPDATED_DATE)
            .importance(UPDATED_IMPORTANCE)
            .commentaire(UPDATED_COMMENTAIRE)
            .incidentPresent(UPDATED_INCIDENT_PRESENT)
            .arretTravail(UPDATED_ARRET_TRAVAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createUpdatedEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        sST.setWorkOrder(workOrder);
        return sST;
    }

    @BeforeEach
    public void initTest() {
        sST = createEntity(em);
    }

    @Test
    @Transactional
    void createSST() throws Exception {
        int databaseSizeBeforeCreate = sSTRepository.findAll().size();
        // Create the SST
        SSTDTO sSTDTO = sSTMapper.toDto(sST);
        restSSTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeCreate + 1);
        SST testSST = sSTList.get(sSTList.size() - 1);
        assertThat(testSST.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testSST.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSST.getImportance()).isEqualTo(DEFAULT_IMPORTANCE);
        assertThat(testSST.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testSST.getIncidentPresent()).isEqualTo(DEFAULT_INCIDENT_PRESENT);
        assertThat(testSST.getArretTravail()).isEqualTo(DEFAULT_ARRET_TRAVAIL);
        assertThat(testSST.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSST.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSST.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSST.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testSST.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testSST.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void createSSTWithExistingId() throws Exception {
        // Create the SST with an existing ID
        sST.setId(1L);
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        int databaseSizeBeforeCreate = sSTRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSSTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = sSTRepository.findAll().size();
        // set the field null
        sST.setLabel(null);

        // Create the SST, which fails.
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        restSSTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = sSTRepository.findAll().size();
        // set the field null
        sST.setDate(null);

        // Create the SST, which fails.
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        restSSTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImportanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = sSTRepository.findAll().size();
        // set the field null
        sST.setImportance(null);

        // Create the SST, which fails.
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        restSSTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncidentPresentIsRequired() throws Exception {
        int databaseSizeBeforeTest = sSTRepository.findAll().size();
        // set the field null
        sST.setIncidentPresent(null);

        // Create the SST, which fails.
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        restSSTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArretTravailIsRequired() throws Exception {
        int databaseSizeBeforeTest = sSTRepository.findAll().size();
        // set the field null
        sST.setArretTravail(null);

        // Create the SST, which fails.
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        restSSTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSSTS() throws Exception {
        // Initialize the database
        sSTRepository.saveAndFlush(sST);

        // Get all the sSTList
        restSSTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sST.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].importance").value(hasItem(DEFAULT_IMPORTANCE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].incidentPresent").value(hasItem(DEFAULT_INCIDENT_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].arretTravail").value(hasItem(DEFAULT_ARRET_TRAVAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdByUserLogin").value(hasItem(DEFAULT_CREATED_BY_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedByUserLogin").value(hasItem(DEFAULT_UPDATED_BY_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getSST() throws Exception {
        // Initialize the database
        sSTRepository.saveAndFlush(sST);

        // Get the sST
        restSSTMockMvc
            .perform(get(ENTITY_API_URL_ID, sST.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sST.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.importance").value(DEFAULT_IMPORTANCE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.incidentPresent").value(DEFAULT_INCIDENT_PRESENT.booleanValue()))
            .andExpect(jsonPath("$.arretTravail").value(DEFAULT_ARRET_TRAVAIL.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdByUserLogin").value(DEFAULT_CREATED_BY_USER_LOGIN))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedByUserLogin").value(DEFAULT_UPDATED_BY_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingSST() throws Exception {
        // Get the sST
        restSSTMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSST() throws Exception {
        // Initialize the database
        sSTRepository.saveAndFlush(sST);

        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();

        // Update the sST
        SST updatedSST = sSTRepository.findById(sST.getId()).get();
        // Disconnect from session so that the updates on updatedSST are not directly saved in db
        em.detach(updatedSST);
        updatedSST
            .label(UPDATED_LABEL)
            .date(UPDATED_DATE)
            .importance(UPDATED_IMPORTANCE)
            .commentaire(UPDATED_COMMENTAIRE)
            .incidentPresent(UPDATED_INCIDENT_PRESENT)
            .arretTravail(UPDATED_ARRET_TRAVAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        SSTDTO sSTDTO = sSTMapper.toDto(updatedSST);

        restSSTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sSTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isOk());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
        SST testSST = sSTList.get(sSTList.size() - 1);
        assertThat(testSST.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testSST.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSST.getImportance()).isEqualTo(UPDATED_IMPORTANCE);
        assertThat(testSST.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testSST.getIncidentPresent()).isEqualTo(UPDATED_INCIDENT_PRESENT);
        assertThat(testSST.getArretTravail()).isEqualTo(UPDATED_ARRET_TRAVAIL);
        assertThat(testSST.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSST.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSST.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSST.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testSST.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testSST.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingSST() throws Exception {
        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();
        sST.setId(count.incrementAndGet());

        // Create the SST
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSSTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sSTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSST() throws Exception {
        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();
        sST.setId(count.incrementAndGet());

        // Create the SST
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSSTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSST() throws Exception {
        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();
        sST.setId(count.incrementAndGet());

        // Create the SST
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSSTMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSSTWithPatch() throws Exception {
        // Initialize the database
        sSTRepository.saveAndFlush(sST);

        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();

        // Update the sST using partial update
        SST partialUpdatedSST = new SST();
        partialUpdatedSST.setId(sST.getId());

        partialUpdatedSST
            .date(UPDATED_DATE)
            .importance(UPDATED_IMPORTANCE)
            .incidentPresent(UPDATED_INCIDENT_PRESENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restSSTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSST.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSST))
            )
            .andExpect(status().isOk());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
        SST testSST = sSTList.get(sSTList.size() - 1);
        assertThat(testSST.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testSST.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSST.getImportance()).isEqualTo(UPDATED_IMPORTANCE);
        assertThat(testSST.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testSST.getIncidentPresent()).isEqualTo(UPDATED_INCIDENT_PRESENT);
        assertThat(testSST.getArretTravail()).isEqualTo(DEFAULT_ARRET_TRAVAIL);
        assertThat(testSST.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSST.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSST.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSST.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testSST.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testSST.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateSSTWithPatch() throws Exception {
        // Initialize the database
        sSTRepository.saveAndFlush(sST);

        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();

        // Update the sST using partial update
        SST partialUpdatedSST = new SST();
        partialUpdatedSST.setId(sST.getId());

        partialUpdatedSST
            .label(UPDATED_LABEL)
            .date(UPDATED_DATE)
            .importance(UPDATED_IMPORTANCE)
            .commentaire(UPDATED_COMMENTAIRE)
            .incidentPresent(UPDATED_INCIDENT_PRESENT)
            .arretTravail(UPDATED_ARRET_TRAVAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restSSTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSST.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSST))
            )
            .andExpect(status().isOk());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
        SST testSST = sSTList.get(sSTList.size() - 1);
        assertThat(testSST.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testSST.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSST.getImportance()).isEqualTo(UPDATED_IMPORTANCE);
        assertThat(testSST.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testSST.getIncidentPresent()).isEqualTo(UPDATED_INCIDENT_PRESENT);
        assertThat(testSST.getArretTravail()).isEqualTo(UPDATED_ARRET_TRAVAIL);
        assertThat(testSST.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSST.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSST.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSST.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testSST.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testSST.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingSST() throws Exception {
        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();
        sST.setId(count.incrementAndGet());

        // Create the SST
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSSTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sSTDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSST() throws Exception {
        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();
        sST.setId(count.incrementAndGet());

        // Create the SST
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSSTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSST() throws Exception {
        int databaseSizeBeforeUpdate = sSTRepository.findAll().size();
        sST.setId(count.incrementAndGet());

        // Create the SST
        SSTDTO sSTDTO = sSTMapper.toDto(sST);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSSTMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sSTDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SST in the database
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSST() throws Exception {
        // Initialize the database
        sSTRepository.saveAndFlush(sST);

        int databaseSizeBeforeDelete = sSTRepository.findAll().size();

        // Delete the sST
        restSSTMockMvc
            .perform(delete(ENTITY_API_URL_ID, sST.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SST> sSTList = sSTRepository.findAll();
        assertThat(sSTList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
