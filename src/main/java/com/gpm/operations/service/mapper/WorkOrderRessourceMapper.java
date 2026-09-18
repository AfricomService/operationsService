package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.WorkOrderRessource;
import com.gpm.operations.service.dto.WorkOrderRessourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkOrderRessource} and its DTO {@link WorkOrderRessourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkOrderRessourceMapper extends EntityMapper<WorkOrderRessourceDTO, WorkOrderRessource> {}
