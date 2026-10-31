package com.gpm.operations.repository;

import com.gpm.operations.domain.ParametrageDeplacement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ParametrageDeplacement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametrageDeplacementRepository extends JpaRepository<ParametrageDeplacement, Long> {
    List<ParametrageDeplacement> findByWorkOrderIdOrderByDateAsc(Long workOrderId);

    void deleteByWorkOrderId(Long workOrderId);
}
