package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.Activite;
import com.gpm.operations.service.dto.ActiviteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activite} and its DTO {@link ActiviteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActiviteMapper extends EntityMapper<ActiviteDTO, Activite> {}
