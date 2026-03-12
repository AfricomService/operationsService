package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.HistoriqueStatutWO;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.service.dto.HistoriqueStatutWODTO;
import com.gpm.operations.service.dto.WorkOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoriqueStatutWO} and its DTO {@link HistoriqueStatutWODTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueStatutWOMapper extends EntityMapper<HistoriqueStatutWODTO, HistoriqueStatutWO> {
    @Mapping(target = "workOrder", source = "workOrder", qualifiedByName = "workOrderId")
    HistoriqueStatutWODTO toDto(HistoriqueStatutWO s);

    @Named("workOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkOrderDTO toDtoWorkOrderId(WorkOrder workOrder);
}
