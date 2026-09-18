package com.gpm.operations.repository;

import com.gpm.operations.domain.WorkOrderRessource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkOrderRessource entity.
 */
@Repository
public interface WorkOrderRessourceRepository extends JpaRepository<WorkOrderRessource, Long> {
    List<WorkOrderRessource> findByWorkOrderId(Long workOrderId);

    List<WorkOrderRessource> findByRessourceIdIn(List<Long> ressourceIds);

    void deleteByWorkOrderId(Long workOrderId);
}
