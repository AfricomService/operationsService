package com.gpm.operations.service;

import com.gpm.operations.domain.MissionArticles;
import com.gpm.operations.repository.MissionArticlesRepository;
import com.gpm.operations.service.dto.MissionArticlesDTO;
import com.gpm.operations.service.mapper.MissionArticlesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MissionArticles}.
 */
@Service
@Transactional
public class MissionArticlesService {

    private final Logger log = LoggerFactory.getLogger(MissionArticlesService.class);

    private final MissionArticlesRepository missionArticlesRepository;

    private final MissionArticlesMapper missionArticlesMapper;

    public MissionArticlesService(MissionArticlesRepository missionArticlesRepository, MissionArticlesMapper missionArticlesMapper) {
        this.missionArticlesRepository = missionArticlesRepository;
        this.missionArticlesMapper = missionArticlesMapper;
    }

    /**
     * Save a missionArticles.
     *
     * @param missionArticlesDTO the entity to save.
     * @return the persisted entity.
     */
    public MissionArticlesDTO save(MissionArticlesDTO missionArticlesDTO) {
        log.debug("Request to save MissionArticles : {}", missionArticlesDTO);
        MissionArticles missionArticles = missionArticlesMapper.toEntity(missionArticlesDTO);
        missionArticles = missionArticlesRepository.save(missionArticles);
        return missionArticlesMapper.toDto(missionArticles);
    }

    /**
     * Update a missionArticles.
     *
     * @param missionArticlesDTO the entity to save.
     * @return the persisted entity.
     */
    public MissionArticlesDTO update(MissionArticlesDTO missionArticlesDTO) {
        log.debug("Request to update MissionArticles : {}", missionArticlesDTO);
        MissionArticles missionArticles = missionArticlesMapper.toEntity(missionArticlesDTO);
        missionArticles = missionArticlesRepository.save(missionArticles);
        return missionArticlesMapper.toDto(missionArticles);
    }

    /**
     * Partially update a missionArticles.
     *
     * @param missionArticlesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MissionArticlesDTO> partialUpdate(MissionArticlesDTO missionArticlesDTO) {
        log.debug("Request to partially update MissionArticles : {}", missionArticlesDTO);

        return missionArticlesRepository
            .findById(missionArticlesDTO.getId())
            .map(existingMissionArticles -> {
                missionArticlesMapper.partialUpdate(existingMissionArticles, missionArticlesDTO);

                return existingMissionArticles;
            })
            .map(missionArticlesRepository::save)
            .map(missionArticlesMapper::toDto);
    }

    /**
     * Get all the missionArticles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MissionArticlesDTO> findAll() {
        log.debug("Request to get all MissionArticles");
        return missionArticlesRepository
            .findAll()
            .stream()
            .map(missionArticlesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one missionArticles by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MissionArticlesDTO> findOne(Long id) {
        log.debug("Request to get MissionArticles : {}", id);
        return missionArticlesRepository.findById(id).map(missionArticlesMapper::toDto);
    }

    /**
     * Delete the missionArticles by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MissionArticles : {}", id);
        missionArticlesRepository.deleteById(id);
    }
}
