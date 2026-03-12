package com.gpm.operations.domain;

import com.gpm.operations.domain.enumeration.StatutWO;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkOrder.
 */
@Entity
@Table(name = "work_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Cross-service FK → Client (projectService)
     */
    @Column(name = "client_id")
    private Long clientId;

    /**
     * Cross-service FK → Affaire (projectService)
     */
    @Column(name = "affaire_id")
    private Long affaireId;

    /**
     * Cross-service FK → Contact (projectService)
     */
    @Column(name = "demandeur_contact_id")
    private Long demandeurContactId;

    /**
     * Cross-service FK → Vehicule (projectService)
     */
    @Column(name = "vehicule_id")
    private Long vehiculeId;

    /**
     * Cross-service FK → OtExterne (financeService)
     */
    @Column(name = "ot_externe_id")
    private Long otExterneId;

    @Column(name = "valideur_id")
    private String valideurId;

    @Column(name = "valideur_user_login")
    private String valideurUserLogin;

    @NotNull
    @Column(name = "date_heure_debut_prev", nullable = false)
    private ZonedDateTime dateHeureDebutPrev;

    @NotNull
    @Column(name = "date_heure_fin_prev", nullable = false)
    private ZonedDateTime dateHeureFinPrev;

    @Column(name = "date_heure_debut_reel")
    private ZonedDateTime dateHeureDebutReel;

    @Column(name = "date_heure_fin_reel")
    private ZonedDateTime dateHeureFinReel;

    @NotNull
    @Column(name = "mission_de_nuit", nullable = false)
    private Boolean missionDeNuit;

    @Column(name = "nombre_nuits")
    private Integer nombreNuits;

    @NotNull
    @Column(name = "hebergement", nullable = false)
    private Boolean hebergement;

    @Column(name = "nombre_hebergements")
    private Integer nombreHebergements;

    @Column(name = "remarque")
    private String remarque;

    @Column(name = "num_fiche_intervention")
    private String numFicheIntervention;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutWO statut;

    @Column(name = "materiel_utilise")
    private String materielUtilise;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_by_user_login")
    private String createdByUserLogin;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_by_user_login")
    private String updatedByUserLogin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public WorkOrder clientId(Long clientId) {
        this.setClientId(clientId);
        return this;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getAffaireId() {
        return this.affaireId;
    }

    public WorkOrder affaireId(Long affaireId) {
        this.setAffaireId(affaireId);
        return this;
    }

    public void setAffaireId(Long affaireId) {
        this.affaireId = affaireId;
    }

    public Long getDemandeurContactId() {
        return this.demandeurContactId;
    }

    public WorkOrder demandeurContactId(Long demandeurContactId) {
        this.setDemandeurContactId(demandeurContactId);
        return this;
    }

    public void setDemandeurContactId(Long demandeurContactId) {
        this.demandeurContactId = demandeurContactId;
    }

    public Long getVehiculeId() {
        return this.vehiculeId;
    }

    public WorkOrder vehiculeId(Long vehiculeId) {
        this.setVehiculeId(vehiculeId);
        return this;
    }

    public void setVehiculeId(Long vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public Long getOtExterneId() {
        return this.otExterneId;
    }

    public WorkOrder otExterneId(Long otExterneId) {
        this.setOtExterneId(otExterneId);
        return this;
    }

    public void setOtExterneId(Long otExterneId) {
        this.otExterneId = otExterneId;
    }

    public String getValideurId() {
        return this.valideurId;
    }

    public WorkOrder valideurId(String valideurId) {
        this.setValideurId(valideurId);
        return this;
    }

    public void setValideurId(String valideurId) {
        this.valideurId = valideurId;
    }

    public String getValideurUserLogin() {
        return this.valideurUserLogin;
    }

    public WorkOrder valideurUserLogin(String valideurUserLogin) {
        this.setValideurUserLogin(valideurUserLogin);
        return this;
    }

    public void setValideurUserLogin(String valideurUserLogin) {
        this.valideurUserLogin = valideurUserLogin;
    }

    public ZonedDateTime getDateHeureDebutPrev() {
        return this.dateHeureDebutPrev;
    }

    public WorkOrder dateHeureDebutPrev(ZonedDateTime dateHeureDebutPrev) {
        this.setDateHeureDebutPrev(dateHeureDebutPrev);
        return this;
    }

    public void setDateHeureDebutPrev(ZonedDateTime dateHeureDebutPrev) {
        this.dateHeureDebutPrev = dateHeureDebutPrev;
    }

    public ZonedDateTime getDateHeureFinPrev() {
        return this.dateHeureFinPrev;
    }

    public WorkOrder dateHeureFinPrev(ZonedDateTime dateHeureFinPrev) {
        this.setDateHeureFinPrev(dateHeureFinPrev);
        return this;
    }

    public void setDateHeureFinPrev(ZonedDateTime dateHeureFinPrev) {
        this.dateHeureFinPrev = dateHeureFinPrev;
    }

    public ZonedDateTime getDateHeureDebutReel() {
        return this.dateHeureDebutReel;
    }

    public WorkOrder dateHeureDebutReel(ZonedDateTime dateHeureDebutReel) {
        this.setDateHeureDebutReel(dateHeureDebutReel);
        return this;
    }

    public void setDateHeureDebutReel(ZonedDateTime dateHeureDebutReel) {
        this.dateHeureDebutReel = dateHeureDebutReel;
    }

    public ZonedDateTime getDateHeureFinReel() {
        return this.dateHeureFinReel;
    }

    public WorkOrder dateHeureFinReel(ZonedDateTime dateHeureFinReel) {
        this.setDateHeureFinReel(dateHeureFinReel);
        return this;
    }

    public void setDateHeureFinReel(ZonedDateTime dateHeureFinReel) {
        this.dateHeureFinReel = dateHeureFinReel;
    }

    public Boolean getMissionDeNuit() {
        return this.missionDeNuit;
    }

    public WorkOrder missionDeNuit(Boolean missionDeNuit) {
        this.setMissionDeNuit(missionDeNuit);
        return this;
    }

    public void setMissionDeNuit(Boolean missionDeNuit) {
        this.missionDeNuit = missionDeNuit;
    }

    public Integer getNombreNuits() {
        return this.nombreNuits;
    }

    public WorkOrder nombreNuits(Integer nombreNuits) {
        this.setNombreNuits(nombreNuits);
        return this;
    }

    public void setNombreNuits(Integer nombreNuits) {
        this.nombreNuits = nombreNuits;
    }

    public Boolean getHebergement() {
        return this.hebergement;
    }

    public WorkOrder hebergement(Boolean hebergement) {
        this.setHebergement(hebergement);
        return this;
    }

    public void setHebergement(Boolean hebergement) {
        this.hebergement = hebergement;
    }

    public Integer getNombreHebergements() {
        return this.nombreHebergements;
    }

    public WorkOrder nombreHebergements(Integer nombreHebergements) {
        this.setNombreHebergements(nombreHebergements);
        return this;
    }

    public void setNombreHebergements(Integer nombreHebergements) {
        this.nombreHebergements = nombreHebergements;
    }

    public String getRemarque() {
        return this.remarque;
    }

    public WorkOrder remarque(String remarque) {
        this.setRemarque(remarque);
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getNumFicheIntervention() {
        return this.numFicheIntervention;
    }

    public WorkOrder numFicheIntervention(String numFicheIntervention) {
        this.setNumFicheIntervention(numFicheIntervention);
        return this;
    }

    public void setNumFicheIntervention(String numFicheIntervention) {
        this.numFicheIntervention = numFicheIntervention;
    }

    public StatutWO getStatut() {
        return this.statut;
    }

    public WorkOrder statut(StatutWO statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutWO statut) {
        this.statut = statut;
    }

    public String getMaterielUtilise() {
        return this.materielUtilise;
    }

    public WorkOrder materielUtilise(String materielUtilise) {
        this.setMaterielUtilise(materielUtilise);
        return this;
    }

    public void setMaterielUtilise(String materielUtilise) {
        this.materielUtilise = materielUtilise;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public WorkOrder createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public WorkOrder updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public WorkOrder createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return this.createdByUserLogin;
    }

    public WorkOrder createdByUserLogin(String createdByUserLogin) {
        this.setCreatedByUserLogin(createdByUserLogin);
        return this;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public WorkOrder updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return this.updatedByUserLogin;
    }

    public WorkOrder updatedByUserLogin(String updatedByUserLogin) {
        this.setUpdatedByUserLogin(updatedByUserLogin);
        return this;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkOrder)) {
            return false;
        }
        return id != null && id.equals(((WorkOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkOrder{" +
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
