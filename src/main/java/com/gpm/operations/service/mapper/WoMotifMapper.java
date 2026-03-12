package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.Motif;
import com.gpm.operations.domain.WoMotif;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.service.dto.MotifDTO;
import com.gpm.operations.service.dto.WoMotifDTO;
import com.gpm.operations.service.dto.WorkOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WoMotif} and its DTO {@link WoMotifDTO}.
 */
@Mapper(componentModel = "spring")
public interface WoMotifMapper extends EntityMapper<WoMotifDTO, WoMotif> {
    @Mapping(target = "workOrder", source = "workOrder", qualifiedByName = "workOrderId")
    @Mapping(target = "motif", source = "motif", qualifiedByName = "motifDesignation")
    WoMotifDTO toDto(WoMotif s);

    @Named("workOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkOrderDTO toDtoWorkOrderId(WorkOrder workOrder);

    @Named("motifDesignation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    MotifDTO toDtoMotifDesignation(Motif motif);
}
