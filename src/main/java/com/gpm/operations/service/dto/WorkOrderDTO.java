package com.gpm.operations.service.dto;

import com.gpm.operations.domain.enumeration.StatutWO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.operations.domain.WorkOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkOrderDTO implements Serializable {

    private Long id;

    /**
     * Cross-service FK → Client (projectService)
     */
    @Schema(description = "Cross-service FK → Client (projectService)")
    private Long clientId;

    /**
     * Cross-service FK → Affaire (projectService)
     */
    @Schema(description = "Cross-service FK → Affaire (projectService)")
    private Long affaireId;

    /**
     * Cross-service FK → Contact (projectService)
     */
    @Schema(description = "Cross-service FK → Contact (projectService)")
    private Long demandeurContactId;

    /**
     * Cross-service FK → Vehicule (projectService)
     */
    @Schema(description = "Cross-service FK → Vehicule (projectService)")
    private Long vehiculeId;

    /**
     * Cross-service FK → OtExterne (financeService)
     */
    @Schema(description = "Cross-service FK → OtExterne (financeService)")
    private Long otExterneId;

    private String valideurId;

    private String valideurUserLogin;

    @NotNull
    private ZonedDateTime dateHeureDebutPrev;

    @NotNull
    private ZonedDateTime dateHeureFinPrev;

    private ZonedDateTime dateHeureDebutReel;

    private ZonedDateTime dateHeureFinReel;

    @NotNull
    private Boolean missionDeNuit;

    private Integer nombreNuits;

    @NotNull
    private Boolean hebergement;

    private Integer nombreHebergements;

    private String remarque;

    private String numFicheIntervention;

    @NotNull
    private StatutWO statut;

    private String materielUtilise;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String createdBy;

    private String createdByUserLogin;

    private String updatedBy;

    private String updatedByUserLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getAffaireId() {
        return affaireId;
    }

    public void setAffaireId(Long affaireId) {
        this.affaireId = affaireId;
    }

    public Long getDemandeurContactId() {
        return demandeurContactId;
    }

    public void setDemandeurContactId(Long demandeurContactId) {
        this.demandeurContactId = demandeurContactId;
    }

    public Long getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Long vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public Long getOtExterneId() {
        return otExterneId;
    }

    public void setOtExterneId(Long otExterneId) {
        this.otExterneId = otExterneId;
    }

    public String getValideurId() {
        return valideurId;
    }

    public void setValideurId(String valideurId) {
        this.valideurId = valideurId;
    }

    public String getValideurUserLogin() {
        return valideurUserLogin;
    }

    public void setValideurUserLogin(String valideurUserLogin) {
        this.valideurUserLogin = valideurUserLogin;
    }

    public ZonedDateTime getDateHeureDebutPrev() {
        return dateHeureDebutPrev;
    }

    public void setDateHeureDebutPrev(ZonedDateTime dateHeureDebutPrev) {
        this.dateHeureDebutPrev = dateHeureDebutPrev;
    }

    public ZonedDateTime getDateHeureFinPrev() {
        return dateHeureFinPrev;
    }

    public void setDateHeureFinPrev(ZonedDateTime dateHeureFinPrev) {
        this.dateHeureFinPrev = dateHeureFinPrev;
    }

    public ZonedDateTime getDateHeureDebutReel() {
        return dateHeureDebutReel;
    }

    public void setDateHeureDebutReel(ZonedDateTime dateHeureDebutReel) {
        this.dateHeureDebutReel = dateHeureDebutReel;
    }

    public ZonedDateTime getDateHeureFinReel() {
        return dateHeureFinReel;
    }

    public void setDateHeureFinReel(ZonedDateTime dateHeureFinReel) {
        this.dateHeureFinReel = dateHeureFinReel;
    }

    public Boolean getMissionDeNuit() {
        return missionDeNuit;
    }

    public void setMissionDeNuit(Boolean missionDeNuit) {
        this.missionDeNuit = missionDeNuit;
    }

    public Integer getNombreNuits() {
        return nombreNuits;
    }

    public void setNombreNuits(Integer nombreNuits) {
        this.nombreNuits = nombreNuits;
    }

    public Boolean getHebergement() {
        return hebergement;
    }

    public void setHebergement(Boolean hebergement) {
        this.hebergement = hebergement;
    }

    public Integer getNombreHebergements() {
        return nombreHebergements;
    }

    public void setNombreHebergements(Integer nombreHebergements) {
        this.nombreHebergements = nombreHebergements;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getNumFicheIntervention() {
        return numFicheIntervention;
    }

    public void setNumFicheIntervention(String numFicheIntervention) {
        this.numFicheIntervention = numFicheIntervention;
    }

    public StatutWO getStatut() {
        return statut;
    }

    public void setStatut(StatutWO statut) {
        this.statut = statut;
    }

    public String getMaterielUtilise() {
        return materielUtilise;
    }

    public void setMaterielUtilise(String materielUtilise) {
        this.materielUtilise = materielUtilise;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return createdByUserLogin;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return updatedByUserLogin;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkOrderDTO)) {
            return false;
        }

        WorkOrderDTO workOrderDTO = (WorkOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkOrderDTO{" +
            "id=" + getId() +
            ", clientId=" + getClientId() +
            ", affaireId=" + getAffaireId() +
            ", demandeurContactId=" + getDemandeurContactId() +
            ", vehiculeId=" + getVehiculeId() +
            ", otExterneId=" + getOtExterneId() +
            ", valideurId='" + getValideurId() + "'" +
            ", valideurUserLogin='" + getValideurUserLogin() + "'" +
            ", dateHeureDebutPrev='" + getDateHeureDebutPrev() + "'" +
            ", dateHeureFinPrev='" + getDateHeureFinPrev() + "'" +
            ", dateHeureDebutReel='" + getDateHeureDebutReel() + "'" +
            ", dateHeureFinReel='" + getDateHeureFinReel() + "'" +
            ", missionDeNuit='" + getMissionDeNuit() + "'" +
            ", nombreNuits=" + getNombreNuits() +
            ", hebergement='" + getHebergement() + "'" +
            ", nombreHebergements=" + getNombreHebergements() +
            ", remarque='" + getRemarque() + "'" +
            ", numFicheIntervention='" + getNumFicheIntervention() + "'" +
            ", statut='" + getStatut() + "'" +
            ", materielUtilise='" + getMaterielUtilise() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdByUserLogin='" + getCreatedByUserLogin() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedByUserLogin='" + getUpdatedByUserLogin() + "'" +
            "}";
    }
}
