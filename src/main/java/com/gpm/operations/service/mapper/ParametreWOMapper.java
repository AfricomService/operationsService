package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.ParametreWO;
import com.gpm.operations.service.dto.ParametreWODTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParametreWO} and its DTO {@link ParametreWODTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParametreWOMapper extends EntityMapper<ParametreWODTO, ParametreWO> {}
