package com.gpm.operations.repository;

import com.gpm.operations.domain.WorkOrderTechniciens;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the WorkOrderTechniciens entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkOrderTechniciensRepository extends JpaRepository<WorkOrderTechniciens, Long> {
    List<WorkOrderTechniciens> findByWorkOrderId(Long workOrderId);

    void deleteByWorkOrderId(Long workOrderId);
}
