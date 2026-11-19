package com.gpm.operations.repository;

import com.gpm.operations.domain.Mission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MissionRepository extends JpaRepository<Mission, Long>, JpaSpecificationExecutor<Mission> {}
