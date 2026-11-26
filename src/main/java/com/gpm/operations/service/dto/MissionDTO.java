package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * A DTO for the {@link com.gpm.operations.domain.Mission} entity.
 */
public class MissionDTO implements Serializable {

    private Long id;

    private String titreBeneficiaire;

    @DecimalMin("0")
    @DecimalMax("20")
    private BigDecimal noteMission;

    private Long contactSocieteId;

    private String contactSocieteMatricule;

    private BigDecimal fraisJourFerie;

    private BigDecimal fraisJourWeekend;

    private BigDecimal fraisPersonnel;

    private BigDecimal montantTotale;

    private Long workorderId;

    private Long noteFraisId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitreBeneficiaire() {
        return titreBeneficiaire;
    }

    public void setTitreBeneficiaire(String titreBeneficiaire) {
        this.titreBeneficiaire = titreBeneficiaire;
    }

    public BigDecimal getNoteMission() {
        return noteMission;
    }

    public void setNoteMission(BigDecimal noteMission) {
        this.noteMission = noteMission;
    }

    public Long getContactSocieteId() {
        return contactSocieteId;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    public String getContactSocieteMatricule() {
        return contactSocieteMatricule;
    }

    public void setContactSocieteMatricule(String contactSocieteMatricule) {
        this.contactSocieteMatricule = contactSocieteMatricule;
    }

    public BigDecimal getFraisJourFerie() {
        return fraisJourFerie;
    }

    public void setFraisJourFerie(BigDecimal fraisJourFerie) {
        this.fraisJourFerie = fraisJourFerie;
    }

    public BigDecimal getFraisJourWeekend() {
        return fraisJourWeekend;
    }

    public void setFraisJourWeekend(BigDecimal fraisJourWeekend) {
        this.fraisJourWeekend = fraisJourWeekend;
    }

    public BigDecimal getFraisPersonnel() {
        return fraisPersonnel;
    }

    public void setFraisPersonnel(BigDecimal fraisPersonnel) {
        this.fraisPersonnel = fraisPersonnel;
    }

    public BigDecimal getMontantTotale() {
        return montantTotale;
    }

    public void setMontantTotale(BigDecimal montantTotale) {
        this.montantTotale = montantTotale;
    }

    public Long getWorkorderId() {
        return workorderId;
    }

    public void setWorkorderId(Long workorderId) {
        this.workorderId = workorderId;
    }

    public Long getNoteFraisId() {
        return noteFraisId;
    }

    public void setNoteFraisId(Long noteFraisId) {
        this.noteFraisId = noteFraisId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MissionDTO)) {
            return false;
        }

        MissionDTO missionDTO = (MissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, missionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MissionDTO{" +
            "id=" + getId() +
            ", titreBeneficiaire='" + getTitreBeneficiaire() + "'" +
            ", noteMission=" + getNoteMission() +
            ", contactSocieteId=" + getContactSocieteId() +
            ", contactSocieteMatricule='" + getContactSocieteMatricule() + "'" +
            ", fraisJourFerie=" + getFraisJourFerie() +
            ", fraisJourWeekend=" + getFraisJourWeekend() +
            ", fraisPersonnel=" + getFraisPersonnel() +
            ", montantTotale=" + getMontantTotale() +
            ", workorderId=" + getWorkorderId() +
            ", noteFraisId=" + getNoteFraisId() +
            "}";
    }
}
