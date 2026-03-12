package com.gpm.operations.repository;

import com.gpm.operations.domain.WorkOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {}
