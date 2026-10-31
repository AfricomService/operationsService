package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.ParametrageDeplacement;
import com.gpm.operations.service.dto.ParametrageDeplacementDTO;
import org.mapstruct.*;


/**
 * Mapper for the entity {@link ParametrageDeplacement} and its DTO {@link ParametrageDeplacementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParametrageDeplacementMapper extends EntityMapper<ParametrageDeplacementDTO, ParametrageDeplacement> {}
