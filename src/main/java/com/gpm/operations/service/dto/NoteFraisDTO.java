package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.operations.domain.NoteFrais} entity.
 */
public class NoteFraisDTO implements Serializable {

    private Long id;

    private ZonedDateTime dateDebut;

    private ZonedDateTime dateFin;

    private Long contactSocieteId;

    private BigDecimal montantTotale;

    private String montantTotaleEnLettre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Long getContactSocieteId() {
        return contactSocieteId;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    public BigDecimal getMontantTotale() {
        return montantTotale;
    }

    public void setMontantTotale(BigDecimal montantTotale) {
        this.montantTotale = montantTotale;
    }

    public String getMontantTotaleEnLettre() {
        return montantTotaleEnLettre;
    }

    public void setMontantTotaleEnLettre(String montantTotaleEnLettre) {
        this.montantTotaleEnLettre = montantTotaleEnLettre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoteFraisDTO)) {
            return false;
        }

        NoteFraisDTO noteFraisDTO = (NoteFraisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, noteFraisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteFraisDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", contactSocieteId=" + getContactSocieteId() +
            ", montantTotale=" + getMontantTotale() +
            ", montantTotaleEnLettre='" + getMontantTotaleEnLettre() + "'" +
            "}";
    }
}
