package com.gpm.operations.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tache.
 */
@Entity
@Table(name = "tache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "role_dans_la_mission", nullable = false)
    private String roleDansLaMission;

    @Column(name = "note")
    private Float note;

    @Column(name = "remboursement")
    private Float remboursement;

    @Column(name = "executeur_id")
    private String executeurId;

    @Column(name = "executeur_user_login")
    private String executeurUserLogin;

    @ManyToOne(optional = false)
    @NotNull
    private WorkOrder workOrder;

    @ManyToOne
    private Activite activite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tache id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleDansLaMission() {
        return this.roleDansLaMission;
    }

    public Tache roleDansLaMission(String roleDansLaMission) {
        this.setRoleDansLaMission(roleDansLaMission);
        return this;
    }

    public void setRoleDansLaMission(String roleDansLaMission) {
        this.roleDansLaMission = roleDansLaMission;
    }

    public Float getNote() {
        return this.note;
    }

    public Tache note(Float note) {
        this.setNote(note);
        return this;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public Float getRemboursement() {
        return this.remboursement;
    }

    public Tache remboursement(Float remboursement) {
        this.setRemboursement(remboursement);
        return this;
    }

    public void setRemboursement(Float remboursement) {
        this.remboursement = remboursement;
    }

    public String getExecuteurId() {
        return this.executeurId;
    }

    public Tache executeurId(String executeurId) {
        this.setExecuteurId(executeurId);
        return this;
    }

    public void setExecuteurId(String executeurId) {
        this.executeurId = executeurId;
    }

    public String getExecuteurUserLogin() {
        return this.executeurUserLogin;
    }

    public Tache executeurUserLogin(String executeurUserLogin) {
        this.setExecuteurUserLogin(executeurUserLogin);
        return this;
    }

    public void setExecuteurUserLogin(String executeurUserLogin) {
        this.executeurUserLogin = executeurUserLogin;
    }

    public WorkOrder getWorkOrder() {
        return this.workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Tache workOrder(WorkOrder workOrder) {
        this.setWorkOrder(workOrder);
        return this;
    }

    public Activite getActivite() {
        return this.activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public Tache activite(Activite activite) {
        this.setActivite(activite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tache)) {
            return false;
        }
        return id != null && id.equals(((Tache) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tache{" +
            "id=" + getId() +
            ", roleDansLaMission='" + getRoleDansLaMission() + "'" +
            ", note=" + getNote() +
            ", remboursement=" + getRemboursement() +
            ", executeurId='" + getExecuteurId() + "'" +
            ", executeurUserLogin='" + getExecuteurUserLogin() + "'" +
            "}";
    }
}
