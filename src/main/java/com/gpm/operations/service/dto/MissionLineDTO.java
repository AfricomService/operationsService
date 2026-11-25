package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Ligne de mission créée lors de l'action « Exécuter » (noteMission optionnelle, null par défaut).
 * Le titre du bénéficiaire (MANAGER / TECHNIQUE) n'est volontairement PAS envoyé par le client : le backend le déduit.
 */
public class MissionLineDTO implements Serializable {

    @NotNull
    private Long contactSocieteId;

    private String contactSocieteMatricule;

    @DecimalMin("0")
    private BigDecimal noteMission;

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

    public BigDecimal getNoteMission() {
        return noteMission;
    }

    public void setNoteMission(BigDecimal noteMission) {
        this.noteMission = noteMission;
    }

    @Override
    public String toString() {
        return (
            "MissionLineDTO{contactSocieteId=" +
                contactSocieteId +
                ", contactSocieteMatricule='" +
                contactSocieteMatricule +
                "', noteMission=" +
                noteMission +
                "}"
        );
    }
}
