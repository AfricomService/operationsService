package com.gpm.operations.repository;

import com.gpm.operations.domain.WorkOrderRessource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkOrderRessource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkOrderRessourceRepository extends JpaRepository<WorkOrderRessource, Long> {}
