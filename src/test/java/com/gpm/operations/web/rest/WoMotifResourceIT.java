package com.gpm.operations.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.operations.IntegrationTest;
import com.gpm.operations.domain.Motif;
import com.gpm.operations.domain.WoMotif;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.repository.WoMotifRepository;
import com.gpm.operations.service.WoMotifService;
import com.gpm.operations.service.dto.WoMotifDTO;
import com.gpm.operations.service.mapper.WoMotifMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WoMotifResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WoMotifResourceIT {

    private static final String ENTITY_API_URL = "/api/wo-motifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WoMotifRepository woMotifRepository;

    @Mock
    private WoMotifRepository woMotifRepositoryMock;

    @Autowired
    private WoMotifMapper woMotifMapper;

    @Mock
    private WoMotifService woMotifServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWoMotifMockMvc;

    private WoMotif woMotif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WoMotif createEntity(EntityManager em) {
        WoMotif woMotif = new WoMotif();
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        woMotif.setWorkOrder(workOrder);
        // Add required entity
        Motif motif;
        if (TestUtil.findAll(em, Motif.class).isEmpty()) {
            motif = MotifResourceIT.createEntity(em);
            em.persist(motif);
            em.flush();
        } else {
            motif = TestUtil.findAll(em, Motif.class).get(0);
        }
        woMotif.setMotif(motif);
        return woMotif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WoMotif createUpdatedEntity(EntityManager em) {
        WoMotif woMotif = new WoMotif();
        // Add required entity
        WorkOrder workOrder;
        if (TestUtil.findAll(em, WorkOrder.class).isEmpty()) {
            workOrder = WorkOrderResourceIT.createUpdatedEntity(em);
            em.persist(workOrder);
            em.flush();
        } else {
            workOrder = TestUtil.findAll(em, WorkOrder.class).get(0);
        }
        woMotif.setWorkOrder(workOrder);
        // Add required entity
        Motif motif;
        if (TestUtil.findAll(em, Motif.class).isEmpty()) {
            motif = MotifResourceIT.createUpdatedEntity(em);
            em.persist(motif);
            em.flush();
        } else {
            motif = TestUtil.findAll(em, Motif.class).get(0);
        }
        woMotif.setMotif(motif);
        return woMotif;
    }

    @BeforeEach
    public void initTest() {
        woMotif = createEntity(em);
    }

    @Test
    @Transactional
    void createWoMotif() throws Exception {
        int databaseSizeBeforeCreate = woMotifRepository.findAll().size();
        // Create the WoMotif
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(woMotif);
        restWoMotifMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeCreate + 1);
        WoMotif testWoMotif = woMotifList.get(woMotifList.size() - 1);
    }

    @Test
    @Transactional
    void createWoMotifWithExistingId() throws Exception {
        // Create the WoMotif with an existing ID
        woMotif.setId(1L);
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(woMotif);

        int databaseSizeBeforeCreate = woMotifRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWoMotifMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWoMotifs() throws Exception {
        // Initialize the database
        woMotifRepository.saveAndFlush(woMotif);

        // Get all the woMotifList
        restWoMotifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(woMotif.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWoMotifsWithEagerRelationshipsIsEnabled() throws Exception {
        when(woMotifServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWoMotifMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(woMotifServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWoMotifsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(woMotifServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWoMotifMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(woMotifRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWoMotif() throws Exception {
        // Initialize the database
        woMotifRepository.saveAndFlush(woMotif);

        // Get the woMotif
        restWoMotifMockMvc
            .perform(get(ENTITY_API_URL_ID, woMotif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(woMotif.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingWoMotif() throws Exception {
        // Get the woMotif
        restWoMotifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWoMotif() throws Exception {
        // Initialize the database
        woMotifRepository.saveAndFlush(woMotif);

        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();

        // Update the woMotif
        WoMotif updatedWoMotif = woMotifRepository.findById(woMotif.getId()).get();
        // Disconnect from session so that the updates on updatedWoMotif are not directly saved in db
        em.detach(updatedWoMotif);
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(updatedWoMotif);

        restWoMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, woMotifDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isOk());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
        WoMotif testWoMotif = woMotifList.get(woMotifList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingWoMotif() throws Exception {
        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();
        woMotif.setId(count.incrementAndGet());

        // Create the WoMotif
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(woMotif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWoMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, woMotifDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWoMotif() throws Exception {
        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();
        woMotif.setId(count.incrementAndGet());

        // Create the WoMotif
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(woMotif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWoMotif() throws Exception {
        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();
        woMotif.setId(count.incrementAndGet());

        // Create the WoMotif
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(woMotif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoMotifMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWoMotifWithPatch() throws Exception {
        // Initialize the database
        woMotifRepository.saveAndFlush(woMotif);

        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();

        // Update the woMotif using partial update
        WoMotif partialUpdatedWoMotif = new WoMotif();
        partialUpdatedWoMotif.setId(woMotif.getId());

        restWoMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWoMotif.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWoMotif))
            )
            .andExpect(status().isOk());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
        WoMotif testWoMotif = woMotifList.get(woMotifList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateWoMotifWithPatch() throws Exception {
        // Initialize the database
        woMotifRepository.saveAndFlush(woMotif);

        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();

        // Update the woMotif using partial update
        WoMotif partialUpdatedWoMotif = new WoMotif();
        partialUpdatedWoMotif.setId(woMotif.getId());

        restWoMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWoMotif.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWoMotif))
            )
            .andExpect(status().isOk());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
        WoMotif testWoMotif = woMotifList.get(woMotifList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingWoMotif() throws Exception {
        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();
        woMotif.setId(count.incrementAndGet());

        // Create the WoMotif
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(woMotif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWoMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, woMotifDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWoMotif() throws Exception {
        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();
        woMotif.setId(count.incrementAndGet());

        // Create the WoMotif
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(woMotif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWoMotif() throws Exception {
        int databaseSizeBeforeUpdate = woMotifRepository.findAll().size();
        woMotif.setId(count.incrementAndGet());

        // Create the WoMotif
        WoMotifDTO woMotifDTO = woMotifMapper.toDto(woMotif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWoMotifMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(woMotifDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WoMotif in the database
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWoMotif() throws Exception {
        // Initialize the database
        woMotifRepository.saveAndFlush(woMotif);

        int databaseSizeBeforeDelete = woMotifRepository.findAll().size();

        // Delete the woMotif
        restWoMotifMockMvc
            .perform(delete(ENTITY_API_URL_ID, woMotif.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WoMotif> woMotifList = woMotifRepository.findAll();
        assertThat(woMotifList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
