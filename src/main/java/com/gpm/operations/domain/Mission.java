package com.gpm.operations.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mission.
 */
@Entity
@Table(name = "mission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titre_beneficiaire")
    private String titreBeneficiaire;

    @Column(name = "note_mission", precision = 21, scale = 2)
    private BigDecimal noteMission;

    @Column(name = "contact_societe_id")
    private Long contactSocieteId;

    @Column(name = "contact_societe_matricule")
    private String contactSocieteMatricule;

    @Column(name = "frais_jour_ferie", precision = 21, scale = 2)
    private BigDecimal fraisJourFerie;

    @Column(name = "frais_jour_weekend", precision = 21, scale = 2)
    private BigDecimal fraisJourWeekend;

    @Column(name = "frais_personnel", precision = 21, scale = 2)
    private BigDecimal fraisPersonnel;

    @Column(name = "montant_totale", precision = 21, scale = 2)
    private BigDecimal montantTotale;

    @Column(name = "workorder_id")
    private Long workorderId;

    @Column(name = "note_frais_id")
    private Long noteFraisId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitreBeneficiaire() {
        return this.titreBeneficiaire;
    }

    public Mission titreBeneficiaire(String titreBeneficiaire) {
        this.setTitreBeneficiaire(titreBeneficiaire);
        return this;
    }

    public void setTitreBeneficiaire(String titreBeneficiaire) {
        this.titreBeneficiaire = titreBeneficiaire;
    }

    public BigDecimal getNoteMission() {
        return this.noteMission;
    }

    public Mission noteMission(BigDecimal noteMission) {
        this.setNoteMission(noteMission);
        return this;
    }

    public void setNoteMission(BigDecimal noteMission) {
        this.noteMission = noteMission;
    }

    public Long getContactSocieteId() {
        return this.contactSocieteId;
    }

    public Mission contactSocieteId(Long contactSocieteId) {
        this.setContactSocieteId(contactSocieteId);
        return this;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    public String getContactSocieteMatricule() {
        return this.contactSocieteMatricule;
    }

    public Mission contactSocieteMatricule(String contactSocieteMatricule) {
        this.setContactSocieteMatricule(contactSocieteMatricule);
        return this;
    }

    public void setContactSocieteMatricule(String contactSocieteMatricule) {
        this.contactSocieteMatricule = contactSocieteMatricule;
    }

    public BigDecimal getFraisJourFerie() {
        return this.fraisJourFerie;
    }

    public Mission fraisJourFerie(BigDecimal fraisJourFerie) {
        this.setFraisJourFerie(fraisJourFerie);
        return this;
    }

    public void setFraisJourFerie(BigDecimal fraisJourFerie) {
        this.fraisJourFerie = fraisJourFerie;
    }

    public BigDecimal getFraisJourWeekend() {
        return this.fraisJourWeekend;
    }

    public Mission fraisJourWeekend(BigDecimal fraisJourWeekend) {
        this.setFraisJourWeekend(fraisJourWeekend);
        return this;
    }

    public void setFraisJourWeekend(BigDecimal fraisJourWeekend) {
        this.fraisJourWeekend = fraisJourWeekend;
    }

    public BigDecimal getFraisPersonnel() {
        return this.fraisPersonnel;
    }

    public Mission fraisPersonnel(BigDecimal fraisPersonnel) {
        this.setFraisPersonnel(fraisPersonnel);
        return this;
    }

    public void setFraisPersonnel(BigDecimal fraisPersonnel) {
        this.fraisPersonnel = fraisPersonnel;
    }

    public BigDecimal getMontantTotale() {
        return this.montantTotale;
    }

    public Mission montantTotale(BigDecimal montantTotale) {
        this.setMontantTotale(montantTotale);
        return this;
    }

    public void setMontantTotale(BigDecimal montantTotale) {
        this.montantTotale = montantTotale;
    }

    public Long getWorkorderId() {
        return this.workorderId;
    }

    public Mission workorderId(Long workorderId) {
        this.setWorkorderId(workorderId);
        return this;
    }

    public void setWorkorderId(Long workorderId) {
        this.workorderId = workorderId;
    }

    public Long getNoteFraisId() {
        return this.noteFraisId;
    }

    public Mission noteFraisId(Long noteFraisId) {
        this.setNoteFraisId(noteFraisId);
        return this;
    }

    public void setNoteFraisId(Long noteFraisId) {
        this.noteFraisId = noteFraisId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mission)) {
            return false;
        }
        return id != null && id.equals(((Mission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mission{" +
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
