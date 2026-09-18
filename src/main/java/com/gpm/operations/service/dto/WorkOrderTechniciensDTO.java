package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.operations.domain.WorkOrderTechniciens} entity.
 */
public class WorkOrderTechniciensDTO implements Serializable {

    private Long id;

    private Long workOrderId;

    private Long contactSocieteId;

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

    public Long getContactSocieteId() {
        return contactSocieteId;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkOrderTechniciensDTO)) {
            return false;
        }

        WorkOrderTechniciensDTO workOrderTechniciensDTO = (WorkOrderTechniciensDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workOrderTechniciensDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkOrderTechniciensDTO{" +
            "id=" + getId() +
            ", workOrderId=" + getWorkOrderId() +
            ", contactSocieteId=" + getContactSocieteId() +
            "}";
    }
}
