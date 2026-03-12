package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.Activite;
import com.gpm.operations.domain.Tache;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.service.dto.ActiviteDTO;
import com.gpm.operations.service.dto.TacheDTO;
import com.gpm.operations.service.dto.WorkOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tache} and its DTO {@link TacheDTO}.
 */
@Mapper(componentModel = "spring")
public interface TacheMapper extends EntityMapper<TacheDTO, Tache> {
    @Mapping(target = "workOrder", source = "workOrder", qualifiedByName = "workOrderId")
    @Mapping(target = "activite", source = "activite", qualifiedByName = "activiteDesignation")
    TacheDTO toDto(Tache s);

    @Named("workOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkOrderDTO toDtoWorkOrderId(WorkOrder workOrder);

    @Named("activiteDesignation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    ActiviteDTO toDtoActiviteDesignation(Activite activite);
}
