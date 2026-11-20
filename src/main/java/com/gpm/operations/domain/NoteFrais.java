package com.gpm.operations.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NoteFrais.
 */
@Entity
@Table(name = "note_frais")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NoteFrais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_debut")
    private ZonedDateTime dateDebut;

    @Column(name = "date_fin")
    private ZonedDateTime dateFin;

    @Column(name = "contact_societe_id")
    private Long contactSocieteId;

    @Column(name = "montant_totale", precision = 21, scale = 2)
    private BigDecimal montantTotale;

    @Column(name = "montant_totale_en_lettre")
    private String montantTotaleEnLettre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NoteFrais id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateDebut() {
        return this.dateDebut;
    }

    public NoteFrais dateDebut(ZonedDateTime dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return this.dateFin;
    }

    public NoteFrais dateFin(ZonedDateTime dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Long getContactSocieteId() {
        return this.contactSocieteId;
    }

    public NoteFrais contactSocieteId(Long contactSocieteId) {
        this.setContactSocieteId(contactSocieteId);
        return this;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    public BigDecimal getMontantTotale() {
        return this.montantTotale;
    }

    public NoteFrais montantTotale(BigDecimal montantTotale) {
        this.setMontantTotale(montantTotale);
        return this;
    }

    public void setMontantTotale(BigDecimal montantTotale) {
        this.montantTotale = montantTotale;
    }

    public String getMontantTotaleEnLettre() {
        return this.montantTotaleEnLettre;
    }

    public NoteFrais montantTotaleEnLettre(String montantTotaleEnLettre) {
        this.setMontantTotaleEnLettre(montantTotaleEnLettre);
        return this;
    }

    public void setMontantTotaleEnLettre(String montantTotaleEnLettre) {
        this.montantTotaleEnLettre = montantTotaleEnLettre;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoteFrais)) {
            return false;
        }
        return id != null && id.equals(((NoteFrais) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteFrais{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", contactSocieteId=" + getContactSocieteId() +
            ", montantTotale=" + getMontantTotale() +
            ", montantTotaleEnLettre='" + getMontantTotaleEnLettre() + "'" +
            "}";
    }
}
