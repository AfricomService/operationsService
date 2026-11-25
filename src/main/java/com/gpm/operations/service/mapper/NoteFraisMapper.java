package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.NoteFrais;
import com.gpm.operations.service.dto.NoteFraisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NoteFrais} and its DTO {@link NoteFraisDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NoteFraisMapper extends EntityMapper<NoteFraisDTO, NoteFrais> {}
