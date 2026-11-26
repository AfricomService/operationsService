package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/** Ligne affichée dans la liste de sélection des missions (mission + infos du work order). */
public class MissionLigneDTO implements Serializable {

    private Long id;
    private String workOrderIdentifiant;
    private Long clientId;
    private ZonedDateTime dateDebut;
    private ZonedDateTime dateFin;
    private String typeMission;
    private BigDecimal montantTotale;

    public MissionLigneDTO(
        Long id,
        String workOrderIdentifiant,
        Long clientId,
        ZonedDateTime dateDebut,
        ZonedDateTime dateFin,
        String typeMission,
        BigDecimal montantTotale
    ) {
        this.id = id;
        this.workOrderIdentifiant = workOrderIdentifiant;
        this.clientId = clientId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeMission = typeMission;
        this.montantTotale = montantTotale;
    }

    public Long getId() { return id; }
    public String getWorkOrderIdentifiant() { return workOrderIdentifiant; }
    public Long getClientId() { return clientId; }
    public ZonedDateTime getDateDebut() { return dateDebut; }
    public ZonedDateTime getDateFin() { return dateFin; }
    public String getTypeMission() { return typeMission; }
    public BigDecimal getMontantTotale() { return montantTotale; }
}
