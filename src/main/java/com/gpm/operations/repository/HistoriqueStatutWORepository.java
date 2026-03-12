package com.gpm.operations.repository;

import com.gpm.operations.domain.HistoriqueStatutWO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoriqueStatutWO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueStatutWORepository extends JpaRepository<HistoriqueStatutWO, Long> {}
