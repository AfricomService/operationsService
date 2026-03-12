package com.gpm.operations.repository;

import com.gpm.operations.domain.WoSite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WoSite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WoSiteRepository extends JpaRepository<WoSite, Long> {}
