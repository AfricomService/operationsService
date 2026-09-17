package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.operations.domain.WorkOrderRessource} entity.
 */
public class WorkOrderRessourceDTO implements Serializable {

    private Long id;

    private Long workOrderId;

    private Long ressourceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getRessourceId() {
        return ressourceId;
    }

    public void setRessourceId(Long ressourceId) {
        this.ressourceId = ressourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkOrderRessourceDTO)) {
            return false;
        }

        WorkOrderRessourceDTO workOrderRessourceDTO = (WorkOrderRessourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workOrderRessourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkOrderRessourceDTO{" +
            "id=" + getId() +
            ", workOrderId=" + getWorkOrderId() +
            ", ressourceId=" + getRessourceId() +
            "}";
    }
}
