package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.WoSite;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.service.dto.WoSiteDTO;
import com.gpm.operations.service.dto.WorkOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WoSite} and its DTO {@link WoSiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface WoSiteMapper extends EntityMapper<WoSiteDTO, WoSite> {
    @Mapping(target = "workOrder", source = "workOrder", qualifiedByName = "workOrderId")
    WoSiteDTO toDto(WoSite s);

    @Named("workOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkOrderDTO toDtoWorkOrderId(WorkOrder workOrder);
}
