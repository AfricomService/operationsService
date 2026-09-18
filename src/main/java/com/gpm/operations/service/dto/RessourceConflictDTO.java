package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * DTO représentant un conflit : une ressource déjà affectée à un autre work order en cours.
 */
public class RessourceConflictDTO implements Serializable {

    private Long ressourceId;
    private Long workOrderId;
    private String numFicheIntervention;
    private String identifiantUnique;
    private ZonedDateTime dateHeureFinPrev;

    public RessourceConflictDTO() {}

    public RessourceConflictDTO(
        Long ressourceId,
        Long workOrderId,
        String numFicheIntervention,
        String identifiantUnique,
        ZonedDateTime dateHeureFinPrev
    ) {
        this.ressourceId = ressourceId;
        this.workOrderId = workOrderId;
        this.numFicheIntervention = numFicheIntervention;
        this.identifiantUnique = identifiantUnique;
        this.dateHeureFinPrev = dateHeureFinPrev;
    }

    public Long getRessourceId() { return ressourceId; }
    public void setRessourceId(Long ressourceId) { this.ressourceId = ressourceId; }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public String getNumFicheIntervention() { return numFicheIntervention; }
    public void setNumFicheIntervention(String numFicheIntervention) { this.numFicheIntervention = numFicheIntervention; }

    public String getIdentifiantUnique() { return identifiantUnique; }
    public void setIdentifiantUnique(String identifiantUnique) { this.identifiantUnique = identifiantUnique; }

    public ZonedDateTime getDateHeureFinPrev() { return dateHeureFinPrev; }
    public void setDateHeureFinPrev(ZonedDateTime dateHeureFinPrev) { this.dateHeureFinPrev = dateHeureFinPrev; }
}
