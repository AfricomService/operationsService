package com.gpm.operations.service;

import com.gpm.operations.domain.HistoriqueStatutWO;
import com.gpm.operations.repository.HistoriqueStatutWORepository;
import com.gpm.operations.service.dto.HistoriqueStatutWODTO;
import com.gpm.operations.service.mapper.HistoriqueStatutWOMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HistoriqueStatutWO}.
 */
@Service
@Transactional
public class HistoriqueStatutWOService {

    private final Logger log = LoggerFactory.getLogger(HistoriqueStatutWOService.class);

    private final HistoriqueStatutWORepository historiqueStatutWORepository;

    private final HistoriqueStatutWOMapper historiqueStatutWOMapper;

    public HistoriqueStatutWOService(
        HistoriqueStatutWORepository historiqueStatutWORepository,
        HistoriqueStatutWOMapper historiqueStatutWOMapper
    ) {
        this.historiqueStatutWORepository = historiqueStatutWORepository;
        this.historiqueStatutWOMapper = historiqueStatutWOMapper;
    }

    /**
     * Save a historiqueStatutWO.
     *
     * @param historiqueStatutWODTO the entity to save.
     * @return the persisted entity.
     */
    public HistoriqueStatutWODTO save(HistoriqueStatutWODTO historiqueStatutWODTO) {
        log.debug("Request to save HistoriqueStatutWO : {}", historiqueStatutWODTO);
        HistoriqueStatutWO historiqueStatutWO = historiqueStatutWOMapper.toEntity(historiqueStatutWODTO);
        historiqueStatutWO = historiqueStatutWORepository.save(historiqueStatutWO);
        return historiqueStatutWOMapper.toDto(historiqueStatutWO);
    }

    /**
     * Update a historiqueStatutWO.
     *
     * @param historiqueStatutWODTO the entity to save.
     * @return the persisted entity.
     */
    public HistoriqueStatutWODTO update(HistoriqueStatutWODTO historiqueStatutWODTO) {
        log.debug("Request to update HistoriqueStatutWO : {}", historiqueStatutWODTO);
        HistoriqueStatutWO historiqueStatutWO = historiqueStatutWOMapper.toEntity(historiqueStatutWODTO);
        historiqueStatutWO = historiqueStatutWORepository.save(historiqueStatutWO);
        return historiqueStatutWOMapper.toDto(historiqueStatutWO);
    }

    /**
     * Partially update a historiqueStatutWO.
     *
     * @param historiqueStatutWODTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HistoriqueStatutWODTO> partialUpdate(HistoriqueStatutWODTO historiqueStatutWODTO) {
        log.debug("Request to partially update HistoriqueStatutWO : {}", historiqueStatutWODTO);

        return historiqueStatutWORepository
            .findById(historiqueStatutWODTO.getId())
            .map(existingHistoriqueStatutWO -> {
                historiqueStatutWOMapper.partialUpdate(existingHistoriqueStatutWO, historiqueStatutWODTO);

                return existingHistoriqueStatutWO;
            })
            .map(historiqueStatutWORepository::save)
            .map(historiqueStatutWOMapper::toDto);
    }

    /**
     * Get all the historiqueStatutWOS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriqueStatutWODTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoriqueStatutWOS");
        return historiqueStatutWORepository.findAll(pageable).map(historiqueStatutWOMapper::toDto);
    }

    /**
     * Get one historiqueStatutWO by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HistoriqueStatutWODTO> findOne(Long id) {
        log.debug("Request to get HistoriqueStatutWO : {}", id);
        return historiqueStatutWORepository.findById(id).map(historiqueStatutWOMapper::toDto);
    }

    /**
     * Delete the historiqueStatutWO by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HistoriqueStatutWO : {}", id);
        historiqueStatutWORepository.deleteById(id);
    }
}
