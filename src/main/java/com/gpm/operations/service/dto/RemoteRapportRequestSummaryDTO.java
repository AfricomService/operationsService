package com.gpm.operations.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteRapportRequestSummaryDTO {

    private Long id;
    private String reference;
    private String dateCreation;
    private String datePlanification;
    private String dateFinTravaux;
    private String nomRapport;
    private String nomTechnicien;
    private String nomEntreprise;
    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    public String getDatePlanification() { return datePlanification; }
    public void setDatePlanification(String datePlanification) { this.datePlanification = datePlanification; }

    public String getDateFinTravaux() { return dateFinTravaux; }
    public void setDateFinTravaux(String dateFinTravaux) { this.dateFinTravaux = dateFinTravaux; }

    public String getNomRapport() { return nomRapport; }
    public void setNomRapport(String nomRapport) { this.nomRapport = nomRapport; }

    public String getNomTechnicien() { return nomTechnicien; }
    public void setNomTechnicien(String nomTechnicien) { this.nomTechnicien = nomTechnicien; }

    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
