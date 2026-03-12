package com.gpm.operations.repository;

import com.gpm.operations.domain.WoMotif;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WoMotif entity.
 */
@Repository
public interface WoMotifRepository extends JpaRepository<WoMotif, Long> {
    default Optional<WoMotif> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<WoMotif> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<WoMotif> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct woMotif from WoMotif woMotif left join fetch woMotif.motif",
        countQuery = "select count(distinct woMotif) from WoMotif woMotif"
    )
    Page<WoMotif> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct woMotif from WoMotif woMotif left join fetch woMotif.motif")
    List<WoMotif> findAllWithToOneRelationships();

    @Query("select woMotif from WoMotif woMotif left join fetch woMotif.motif where woMotif.id =:id")
    Optional<WoMotif> findOneWithToOneRelationships(@Param("id") Long id);
}
