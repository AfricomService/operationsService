package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * DTO représentant un conflit : un véhicule déjà affecté à un autre work order en cours.
 */
public class VehiculeConflictDTO implements Serializable {

    private Long vehiculeId;
    private Long workOrderId;
    private String numFicheIntervention;
    private String identifiantUnique;
    private ZonedDateTime dateHeureFinPrev;

    public VehiculeConflictDTO() {}

    public VehiculeConflictDTO(
        Long vehiculeId,
        Long workOrderId,
        String numFicheIntervention,
        String identifiantUnique,
        ZonedDateTime dateHeureFinPrev
    ) {
        this.vehiculeId = vehiculeId;
        this.workOrderId = workOrderId;
        this.numFicheIntervention = numFicheIntervention;
        this.identifiantUnique = identifiantUnique;
        this.dateHeureFinPrev = dateHeureFinPrev;
    }

    public Long getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Long vehiculeId) { this.vehiculeId = vehiculeId; }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public String getNumFicheIntervention() { return numFicheIntervention; }
    public void setNumFicheIntervention(String numFicheIntervention) { this.numFicheIntervention = numFicheIntervention; }

    public String getIdentifiantUnique() { return identifiantUnique; }
    public void setIdentifiantUnique(String identifiantUnique) { this.identifiantUnique = identifiantUnique; }

    public ZonedDateTime getDateHeureFinPrev() { return dateHeureFinPrev; }
    public void setDateHeureFinPrev(ZonedDateTime dateHeureFinPrev) { this.dateHeureFinPrev = dateHeureFinPrev; }
}
