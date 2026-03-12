package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.SST;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.service.dto.SSTDTO;
import com.gpm.operations.service.dto.WorkOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SST} and its DTO {@link SSTDTO}.
 */
@Mapper(componentModel = "spring")
public interface SSTMapper extends EntityMapper<SSTDTO, SST> {
    @Mapping(target = "workOrder", source = "workOrder", qualifiedByName = "workOrderId")
    SSTDTO toDto(SST s);

    @Named("workOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkOrderDTO toDtoWorkOrderId(WorkOrder workOrder);
}
