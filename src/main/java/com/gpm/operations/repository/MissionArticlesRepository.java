package com.gpm.operations.repository;

import com.gpm.operations.domain.MissionArticles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MissionArticles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MissionArticlesRepository extends JpaRepository<MissionArticles, Long> {}
