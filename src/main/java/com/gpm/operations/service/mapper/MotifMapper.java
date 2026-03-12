package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.Motif;
import com.gpm.operations.service.dto.MotifDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Motif} and its DTO {@link MotifDTO}.
 */
@Mapper(componentModel = "spring")
public interface MotifMapper extends EntityMapper<MotifDTO, Motif> {}
