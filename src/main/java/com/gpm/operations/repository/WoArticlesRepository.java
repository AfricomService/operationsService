package com.gpm.operations.repository;

import com.gpm.operations.domain.WoArticles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WoArticles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WoArticlesRepository extends JpaRepository<WoArticles, Long> {}
