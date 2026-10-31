//package com.gpm.operations.web.rest;
//
//import static com.gpm.operations.web.rest.TestUtil.sameInstant;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.gpm.operations.IntegrationTest;
//import com.gpm.operations.domain.ParametrageDeplacement;
//import com.gpm.operations.repository.ParametrageDeplacementRepository;
//import com.gpm.operations.service.dto.ParametrageDeplacementDTO;
//import com.gpm.operations.service.mapper.ParametrageDeplacementMapper;
//import java.time.Instant;
//import java.time.ZoneId;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link ParametrageDeplacementResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class ParametrageDeplacementResourceIT {
//
//    private static final Long DEFAULT_WORK_ORDER_ID = 1L;
//    private static final Long UPDATED_WORK_ORDER_ID = 2L;
//
//    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final String DEFAULT_PARAMS = "AAAAAAAAAA";
//    private static final String UPDATED_PARAMS = "BBBBBBBBBB";
//
//    private static final String ENTITY_API_URL = "/api/parametrage-deplacements";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private ParametrageDeplacementRepository parametrageDeplacementRepository;
//
//    @Autowired
//    private ParametrageDeplacementMapper parametrageDeplacementMapper;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restParametrageDeplacementMockMvc;
//
//    private ParametrageDeplacement parametrageDeplacement;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static ParametrageDeplacement createEntity(EntityManager em) {
//        ParametrageDeplacement parametrageDeplacement = new ParametrageDeplacement()
//            .workOrderId(DEFAULT_WORK_ORDER_ID)
//            .date(DEFAULT_DATE)
//            .params(DEFAULT_PARAMS);
//        return parametrageDeplacement;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static ParametrageDeplacement createUpdatedEntity(EntityManager em) {
//        ParametrageDeplacement parametrageDeplacement = new ParametrageDeplacement()
//            .workOrderId(UPDATED_WORK_ORDER_ID)
//            .date(UPDATED_DATE)
//            .params(UPDATED_PARAMS);
//        return parametrageDeplacement;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        parametrageDeplacement = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createParametrageDeplacement() throws Exception {
//        int databaseSizeBeforeCreate = parametrageDeplacementRepository.findAll().size();
//        // Create the ParametrageDeplacement
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(parametrageDeplacement);
//        restParametrageDeplacementMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isCreated());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeCreate + 1);
//        ParametrageDeplacement testParametrageDeplacement = parametrageDeplacementList.get(parametrageDeplacementList.size() - 1);
//        assertThat(testParametrageDeplacement.getWorkOrderId()).isEqualTo(DEFAULT_WORK_ORDER_ID);
//        assertThat(testParametrageDeplacement.getDate()).isEqualTo(DEFAULT_DATE);
//        assertThat(testParametrageDeplacement.getParams()).isEqualTo(DEFAULT_PARAMS);
//    }
//
//    @Test
//    @Transactional
//    void createParametrageDeplacementWithExistingId() throws Exception {
//        // Create the ParametrageDeplacement with an existing ID
//        parametrageDeplacement.setId(1L);
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(parametrageDeplacement);
//
//        int databaseSizeBeforeCreate = parametrageDeplacementRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restParametrageDeplacementMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void getAllParametrageDeplacements() throws Exception {
//        // Initialize the database
//        parametrageDeplacementRepository.saveAndFlush(parametrageDeplacement);
//
//        // Get all the parametrageDeplacementList
//        restParametrageDeplacementMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(parametrageDeplacement.getId().intValue())))
//            .andExpect(jsonPath("$.[*].workOrderId").value(hasItem(DEFAULT_WORK_ORDER_ID.intValue())))
//            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
//            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS)));
//    }
//
//    @Test
//    @Transactional
//    void getParametrageDeplacement() throws Exception {
//        // Initialize the database
//        parametrageDeplacementRepository.saveAndFlush(parametrageDeplacement);
//
//        // Get the parametrageDeplacement
//        restParametrageDeplacementMockMvc
//            .perform(get(ENTITY_API_URL_ID, parametrageDeplacement.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(parametrageDeplacement.getId().intValue()))
//            .andExpect(jsonPath("$.workOrderId").value(DEFAULT_WORK_ORDER_ID.intValue()))
//            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
//            .andExpect(jsonPath("$.params").value(DEFAULT_PARAMS));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingParametrageDeplacement() throws Exception {
//        // Get the parametrageDeplacement
//        restParametrageDeplacementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewParametrageDeplacement() throws Exception {
//        // Initialize the database
//        parametrageDeplacementRepository.saveAndFlush(parametrageDeplacement);
//
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//
//        // Update the parametrageDeplacement
//        ParametrageDeplacement updatedParametrageDeplacement = parametrageDeplacementRepository
//            .findById(parametrageDeplacement.getId())
//            .get();
//        // Disconnect from session so that the updates on updatedParametrageDeplacement are not directly saved in db
//        em.detach(updatedParametrageDeplacement);
//        updatedParametrageDeplacement.workOrderId(UPDATED_WORK_ORDER_ID).date(UPDATED_DATE).params(UPDATED_PARAMS);
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(updatedParametrageDeplacement);
//
//        restParametrageDeplacementMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, parametrageDeplacementDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//        ParametrageDeplacement testParametrageDeplacement = parametrageDeplacementList.get(parametrageDeplacementList.size() - 1);
//        assertThat(testParametrageDeplacement.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
//        assertThat(testParametrageDeplacement.getDate()).isEqualTo(UPDATED_DATE);
//        assertThat(testParametrageDeplacement.getParams()).isEqualTo(UPDATED_PARAMS);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingParametrageDeplacement() throws Exception {
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//        parametrageDeplacement.setId(count.incrementAndGet());
//
//        // Create the ParametrageDeplacement
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(parametrageDeplacement);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restParametrageDeplacementMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, parametrageDeplacementDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchParametrageDeplacement() throws Exception {
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//        parametrageDeplacement.setId(count.incrementAndGet());
//
//        // Create the ParametrageDeplacement
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(parametrageDeplacement);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restParametrageDeplacementMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamParametrageDeplacement() throws Exception {
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//        parametrageDeplacement.setId(count.incrementAndGet());
//
//        // Create the ParametrageDeplacement
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(parametrageDeplacement);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restParametrageDeplacementMockMvc
//            .perform(
//                put(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateParametrageDeplacementWithPatch() throws Exception {
//        // Initialize the database
//        parametrageDeplacementRepository.saveAndFlush(parametrageDeplacement);
//
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//
//        // Update the parametrageDeplacement using partial update
//        ParametrageDeplacement partialUpdatedParametrageDeplacement = new ParametrageDeplacement();
//        partialUpdatedParametrageDeplacement.setId(parametrageDeplacement.getId());
//
//        partialUpdatedParametrageDeplacement.date(UPDATED_DATE).params(UPDATED_PARAMS);
//
//        restParametrageDeplacementMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedParametrageDeplacement.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametrageDeplacement))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//        ParametrageDeplacement testParametrageDeplacement = parametrageDeplacementList.get(parametrageDeplacementList.size() - 1);
//        assertThat(testParametrageDeplacement.getWorkOrderId()).isEqualTo(DEFAULT_WORK_ORDER_ID);
//        assertThat(testParametrageDeplacement.getDate()).isEqualTo(UPDATED_DATE);
//        assertThat(testParametrageDeplacement.getParams()).isEqualTo(UPDATED_PARAMS);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateParametrageDeplacementWithPatch() throws Exception {
//        // Initialize the database
//        parametrageDeplacementRepository.saveAndFlush(parametrageDeplacement);
//
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//
//        // Update the parametrageDeplacement using partial update
//        ParametrageDeplacement partialUpdatedParametrageDeplacement = new ParametrageDeplacement();
//        partialUpdatedParametrageDeplacement.setId(parametrageDeplacement.getId());
//
//        partialUpdatedParametrageDeplacement.workOrderId(UPDATED_WORK_ORDER_ID).date(UPDATED_DATE).params(UPDATED_PARAMS);
//
//        restParametrageDeplacementMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedParametrageDeplacement.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametrageDeplacement))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//        ParametrageDeplacement testParametrageDeplacement = parametrageDeplacementList.get(parametrageDeplacementList.size() - 1);
//        assertThat(testParametrageDeplacement.getWorkOrderId()).isEqualTo(UPDATED_WORK_ORDER_ID);
//        assertThat(testParametrageDeplacement.getDate()).isEqualTo(UPDATED_DATE);
//        assertThat(testParametrageDeplacement.getParams()).isEqualTo(UPDATED_PARAMS);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingParametrageDeplacement() throws Exception {
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//        parametrageDeplacement.setId(count.incrementAndGet());
//
//        // Create the ParametrageDeplacement
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(parametrageDeplacement);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restParametrageDeplacementMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, parametrageDeplacementDTO.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchParametrageDeplacement() throws Exception {
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//        parametrageDeplacement.setId(count.incrementAndGet());
//
//        // Create the ParametrageDeplacement
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(parametrageDeplacement);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restParametrageDeplacementMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamParametrageDeplacement() throws Exception {
//        int databaseSizeBeforeUpdate = parametrageDeplacementRepository.findAll().size();
//        parametrageDeplacement.setId(count.incrementAndGet());
//
//        // Create the ParametrageDeplacement
//        ParametrageDeplacementDTO parametrageDeplacementDTO = parametrageDeplacementMapper.toDto(parametrageDeplacement);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restParametrageDeplacementMockMvc
//            .perform(
//                patch(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(parametrageDeplacementDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the ParametrageDeplacement in the database
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteParametrageDeplacement() throws Exception {
//        // Initialize the database
//        parametrageDeplacementRepository.saveAndFlush(parametrageDeplacement);
//
//        int databaseSizeBeforeDelete = parametrageDeplacementRepository.findAll().size();
//
//        // Delete the parametrageDeplacement
//        restParametrageDeplacementMockMvc
//            .perform(delete(ENTITY_API_URL_ID, parametrageDeplacement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<ParametrageDeplacement> parametrageDeplacementList = parametrageDeplacementRepository.findAll();
//        assertThat(parametrageDeplacementList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
