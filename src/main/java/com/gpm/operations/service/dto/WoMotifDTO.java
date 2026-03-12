package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.operations.domain.WoMotif} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WoMotifDTO implements Serializable {

    private Long id;

    private WorkOrderDTO workOrder;

    private MotifDTO motif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkOrderDTO getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrderDTO workOrder) {
        this.workOrder = workOrder;
    }

    public MotifDTO getMotif() {
        return motif;
    }

    public void setMotif(MotifDTO motif) {
        this.motif = motif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WoMotifDTO)) {
            return false;
        }

        WoMotifDTO woMotifDTO = (WoMotifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, woMotifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WoMotifDTO{" +
            "id=" + getId() +
            ", workOrder=" + getWorkOrder() +
            ", motif=" + getMotif() +
            "}";
    }
}
