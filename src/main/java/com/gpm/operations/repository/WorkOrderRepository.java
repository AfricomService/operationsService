package com.gpm.operations.repository;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.StatutWO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    /**
     * Récupère les WorkOrders en filtrant optionnellement par statut et/ou par client.
     * Un paramètre à {@code null} désactive le filtre correspondant (JPQL évalue
     * ":param is null" avant de comparer, donc le filtre est simplement ignoré).
     */
    @Query(
        "select w from WorkOrder w " +
            "where (:statut is null or w.statut = :statut) " +
            "and (:clientId is null or w.clientId = :clientId) " +
            "and (:search = '' " +
            "     or lower(w.identifiantUnique) like lower(concat('%', :search, '%')) " +
            "     or w.affaireId in (:affaireIds))"
    )
    Page<WorkOrder> findAllWithFilters(
        @Param("statut") StatutWO statut,
        @Param("clientId") Long clientId,
        @Param("search") String search,
        @Param("affaireIds") List<Long> affaireIds,
        Pageable pageable
    );
}
