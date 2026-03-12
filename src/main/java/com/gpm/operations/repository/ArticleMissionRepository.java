package com.gpm.operations.repository;

import com.gpm.operations.domain.ArticleMission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticleMission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleMissionRepository extends JpaRepository<ArticleMission, Long> {}
