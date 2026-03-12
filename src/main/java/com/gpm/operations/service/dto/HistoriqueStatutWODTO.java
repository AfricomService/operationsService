package com.gpm.operations.service.dto;

import com.gpm.operations.domain.enumeration.StatutWO;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.operations.domain.HistoriqueStatutWO} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueStatutWODTO implements Serializable {

    private Long id;

    @NotNull
    private StatutWO ancienStatut;

    @NotNull
    private StatutWO nouveauStatut;

    @NotNull
    private ZonedDateTime dateChangement;

    private String commentaire;

    private String userId;

    private String userLogin;

    private WorkOrderDTO workOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutWO getAncienStatut() {
        return ancienStatut;
    }

    public void setAncienStatut(StatutWO ancienStatut) {
        this.ancienStatut = ancienStatut;
    }

    public StatutWO getNouveauStatut() {
        return nouveauStatut;
    }

    public void setNouveauStatut(StatutWO nouveauStatut) {
        this.nouveauStatut = nouveauStatut;
    }

    public ZonedDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(ZonedDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public WorkOrderDTO getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrderDTO workOrder) {
        this.workOrder = workOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueStatutWODTO)) {
            return false;
        }

        HistoriqueStatutWODTO historiqueStatutWODTO = (HistoriqueStatutWODTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueStatutWODTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueStatutWODTO{" +
            "id=" + getId() +
            ", ancienStatut='" + getAncienStatut() + "'" +
            ", nouveauStatut='" + getNouveauStatut() + "'" +
            ", dateChangement='" + getDateChangement() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", workOrder=" + getWorkOrder() +
            "}";
    }
}
