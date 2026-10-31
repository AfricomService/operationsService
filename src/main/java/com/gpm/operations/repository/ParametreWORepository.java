package com.gpm.operations.repository;

import com.gpm.operations.domain.ParametreWO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ParametreWO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametreWORepository extends JpaRepository<ParametreWO, Long> {}
