package com.gpm.operations.repository;

import com.gpm.operations.domain.AclSid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AclSid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclSidRepository extends JpaRepository<AclSid, Long> {
    Optional<AclSid> findBySidTypeAndSidValue(String sidType, String sidValue);

    List<AclSid> findByIdIn(List<Long> ids);
}
