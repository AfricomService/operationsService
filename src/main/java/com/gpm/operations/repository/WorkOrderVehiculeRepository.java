package com.gpm.operations.repository;

import com.gpm.operations.domain.WorkOrderVehicule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the WorkOrderVehicule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkOrderVehiculeRepository extends JpaRepository<WorkOrderVehicule, Long> {
    List<WorkOrderVehicule> findByWorkOrderId(Long workOrderId);

    List<WorkOrderVehicule> findByVehiculeIdIn(List<Long> vehiculeIds);

    void deleteByWorkOrderId(Long workOrderId);
}
