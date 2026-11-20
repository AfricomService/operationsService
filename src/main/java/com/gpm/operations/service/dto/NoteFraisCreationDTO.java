package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class NoteFraisCreationDTO implements Serializable {

    private ZonedDateTime dateDebut;
    private ZonedDateTime dateFin;
    private Long contactSocieteId;
    private List<Long> missionIds;

    public ZonedDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(ZonedDateTime dateDebut) { this.dateDebut = dateDebut; }
    public ZonedDateTime getDateFin() { return dateFin; }
    public void setDateFin(ZonedDateTime dateFin) { this.dateFin = dateFin; }
    public Long getContactSocieteId() { return contactSocieteId; }
    public void setContactSocieteId(Long contactSocieteId) { this.contactSocieteId = contactSocieteId; }
    public List<Long> getMissionIds() { return missionIds; }
    public void setMissionIds(List<Long> missionIds) { this.missionIds = missionIds; }
}
