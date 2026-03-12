package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.ArticleMission;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.service.dto.ArticleMissionDTO;
import com.gpm.operations.service.dto.WorkOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArticleMission} and its DTO {@link ArticleMissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleMissionMapper extends EntityMapper<ArticleMissionDTO, ArticleMission> {
    @Mapping(target = "workOrder", source = "workOrder", qualifiedByName = "workOrderId")
    ArticleMissionDTO toDto(ArticleMission s);

    @Named("workOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkOrderDTO toDtoWorkOrderId(WorkOrder workOrder);
}
