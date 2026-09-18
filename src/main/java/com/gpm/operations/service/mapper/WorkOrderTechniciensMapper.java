package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.WorkOrderTechniciens;
import com.gpm.operations.service.dto.WorkOrderTechniciensDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkOrderTechniciens} and its DTO {@link WorkOrderTechniciensDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkOrderTechniciensMapper extends EntityMapper<WorkOrderTechniciensDTO, WorkOrderTechniciens> {}
