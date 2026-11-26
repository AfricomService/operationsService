package com.gpm.operations.repository;

import com.gpm.operations.domain.NoteFrais;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NoteFrais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteFraisRepository extends JpaRepository<NoteFrais, Long> {}
