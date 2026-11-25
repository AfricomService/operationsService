package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.Mission;
import com.gpm.operations.service.dto.MissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mission} and its DTO {@link MissionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MissionMapper extends EntityMapper<MissionDTO, Mission> {}
