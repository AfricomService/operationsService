package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.WorkOrderVehicule;
import com.gpm.operations.service.dto.WorkOrderVehiculeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkOrderVehicule} and its DTO {@link WorkOrderVehiculeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkOrderVehiculeMapper extends EntityMapper<WorkOrderVehiculeDTO, WorkOrderVehicule> {}
