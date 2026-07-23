package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.MissionArticles;
import com.gpm.operations.service.dto.MissionArticlesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MissionArticles} and its DTO {@link MissionArticlesDTO}.
 */
@Mapper(componentModel = "spring")
public interface MissionArticlesMapper extends EntityMapper<MissionArticlesDTO, MissionArticles> {}
