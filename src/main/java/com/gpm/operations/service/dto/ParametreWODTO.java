package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.operations.domain.ParametreWO} entity.
 */
public class ParametreWODTO implements Serializable {

    private Long id;

    private String nom;

    private String description;

    private BigDecimal montantTechnicien;

    private BigDecimal montantChefTechnicien;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMontantTechnicien() {
        return montantTechnicien;
    }

    public void setMontantTechnicien(BigDecimal montantTechnicien) {
        this.montantTechnicien = montantTechnicien;
    }

    public BigDecimal getMontantChefTechnicien() {
        return montantChefTechnicien;
    }

    public void setMontantChefTechnicien(BigDecimal montantChefTechnicien) {
        this.montantChefTechnicien = montantChefTechnicien;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParametreWODTO)) {
            return false;
        }

        ParametreWODTO parametreWODTO = (ParametreWODTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parametreWODTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametreWODTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", montantTechnicien=" + getMontantTechnicien() +
            ", montantChefTechnicien=" + getMontantChefTechnicien() +
            "}";
    }
}
