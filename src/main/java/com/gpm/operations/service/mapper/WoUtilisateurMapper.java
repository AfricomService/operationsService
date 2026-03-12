package com.gpm.operations.service.mapper;

import com.gpm.operations.domain.WoUtilisateur;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.service.dto.WoUtilisateurDTO;
import com.gpm.operations.service.dto.WorkOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WoUtilisateur} and its DTO {@link WoUtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface WoUtilisateurMapper extends EntityMapper<WoUtilisateurDTO, WoUtilisateur> {
    @Mapping(target = "workOrder", source = "workOrder", qualifiedByName = "workOrderId")
    WoUtilisateurDTO toDto(WoUtilisateur s);

    @Named("workOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkOrderDTO toDtoWorkOrderId(WorkOrder workOrder);
}
