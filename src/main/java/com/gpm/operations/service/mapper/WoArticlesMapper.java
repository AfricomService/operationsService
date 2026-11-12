package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.WoArticles;
import com.gpm.operations.service.dto.WoArticlesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WoArticles} and its DTO {@link WoArticlesDTO}.
 */
@Mapper(componentModel = "spring")
public interface WoArticlesMapper extends EntityMapper<WoArticlesDTO, WoArticles> {}
