package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.operations.domain.WorkOrderVehicule} entity.
 */
public class WorkOrderVehiculeDTO implements Serializable {

    private Long id;

    private Long workOrderId;

    private Long vehiculeId;

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

    public Long getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Long vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkOrderVehiculeDTO)) {
            return false;
        }

        WorkOrderVehiculeDTO workOrderVehiculeDTO = (WorkOrderVehiculeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workOrderVehiculeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkOrderVehiculeDTO{" +
            "id=" + getId() +
            ", workOrderId=" + getWorkOrderId() +
            ", vehiculeId=" + getVehiculeId() +
            "}";
    }
}
