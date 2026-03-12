package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.operations.domain.Tache} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TacheDTO implements Serializable {

    private Long id;

    @NotNull
    private String roleDansLaMission;

    private Float note;

    private Float remboursement;

    private String executeurId;

    private String executeurUserLogin;

    private WorkOrderDTO workOrder;

    private ActiviteDTO activite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleDansLaMission() {
        return roleDansLaMission;
    }

    public void setRoleDansLaMission(String roleDansLaMission) {
        this.roleDansLaMission = roleDansLaMission;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public Float getRemboursement() {
        return remboursement;
    }

    public void setRemboursement(Float remboursement) {
        this.remboursement = remboursement;
    }

    public String getExecuteurId() {
        return executeurId;
    }

    public void setExecuteurId(String executeurId) {
        this.executeurId = executeurId;
    }

    public String getExecuteurUserLogin() {
        return executeurUserLogin;
    }

    public void setExecuteurUserLogin(String executeurUserLogin) {
        this.executeurUserLogin = executeurUserLogin;
    }

    public WorkOrderDTO getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrderDTO workOrder) {
        this.workOrder = workOrder;
    }

    public ActiviteDTO getActivite() {
        return activite;
    }

    public void setActivite(ActiviteDTO activite) {
        this.activite = activite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TacheDTO)) {
            return false;
        }

        TacheDTO tacheDTO = (TacheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tacheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TacheDTO{" +
            "id=" + getId() +
            ", roleDansLaMission='" + getRoleDansLaMission() + "'" +
            ", note=" + getNote() +
            ", remboursement=" + getRemboursement() +
            ", executeurId='" + getExecuteurId() + "'" +
            ", executeurUserLogin='" + getExecuteurUserLogin() + "'" +
            ", workOrder=" + getWorkOrder() +
            ", activite=" + getActivite() +
            "}";
    }
}
