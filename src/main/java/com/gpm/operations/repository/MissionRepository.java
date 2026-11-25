package com.gpm.operations.repository;

import com.gpm.operations.domain.Mission;
import com.gpm.operations.service.dto.MissionLigneDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data SQL repository for the Mission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MissionRepository extends JpaRepository<Mission, Long>, JpaSpecificationExecutor<Mission> {
    @Query(
        "select new com.gpm.operations.service.dto.MissionLigneDTO(" +
            "m.id, w.identifiantUnique, w.clientId, w.dateHeureDebutReel, w.dateHeureFinReel, w.typeMission, m.montantTotale) " +
            "from Mission m, WorkOrder w " +
            "where m.workorderId = w.id " +
            "and w.dateHeureDebutReel >= :debut and w.dateHeureDebutReel <= :fin " +
            "and (:contactSocieteId is null or m.contactSocieteId = :contactSocieteId) " +
            "and m.noteFraisId is null " +
            "order by w.dateHeureDebutReel asc"
    )
    List<MissionLigneDTO> findByPeriodeWorkOrder(
        @Param("debut") ZonedDateTime debut,
        @Param("fin") ZonedDateTime fin,
        @Param("contactSocieteId") Long contactSocieteId
    );

    List<Mission> findByWorkorderId(Long workorderId);
}
