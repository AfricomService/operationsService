package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.service.dto.WorkOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkOrder} and its DTO {@link WorkOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkOrderMapper extends EntityMapper<WorkOrderDTO, WorkOrder> {}
