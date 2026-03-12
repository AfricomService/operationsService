package com.gpm.operations.repository;

import com.gpm.operations.domain.WoUtilisateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WoUtilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WoUtilisateurRepository extends JpaRepository<WoUtilisateur, Long> {}
