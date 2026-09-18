package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class TechnicienConflictDTO implements Serializable {

    private Long contactSocieteId;
    private Long workOrderId;
    private String numFicheIntervention;
    private String identifiantUnique;
    private ZonedDateTime dateHeureFinPrev;

    public TechnicienConflictDTO() {}

    public TechnicienConflictDTO(
        Long contactSocieteId,
        Long workOrderId,
        String numFicheIntervention,
        String identifiantUnique,
        ZonedDateTime dateHeureFinPrev
    ) {
        this.contactSocieteId = contactSocieteId;
        this.workOrderId = workOrderId;
        this.numFicheIntervention = numFicheIntervention;
        this.identifiantUnique = identifiantUnique;
        this.dateHeureFinPrev = dateHeureFinPrev;
    }

    public Long getContactSocieteId() { return contactSocieteId; }
    public void setContactSocieteId(Long contactSocieteId) { this.contactSocieteId = contactSocieteId; }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public String getNumFicheIntervention() { return numFicheIntervention; }
    public void setNumFicheIntervention(String numFicheIntervention) { this.numFicheIntervention = numFicheIntervention; }

    public String getIdentifiantUnique() { return identifiantUnique; }
    public void setIdentifiantUnique(String identifiantUnique) { this.identifiantUnique = identifiantUnique; }

    public ZonedDateTime getDateHeureFinPrev() { return dateHeureFinPrev; }
    public void setDateHeureFinPrev(ZonedDateTime dateHeureFinPrev) { this.dateHeureFinPrev = dateHeureFinPrev; }
}
