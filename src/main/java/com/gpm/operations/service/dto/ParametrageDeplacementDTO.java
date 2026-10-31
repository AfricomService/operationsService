package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.operations.domain.ParametrageDeplacement} entity.
 */
public class ParametrageDeplacementDTO implements Serializable {

    private Long id;

    private Long workOrderId;

    private ZonedDateTime date;

    private Map<String, Object> params;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParametrageDeplacementDTO)) {
            return false;
        }

        ParametrageDeplacementDTO parametrageDeplacementDTO = (ParametrageDeplacementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parametrageDeplacementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametrageDeplacementDTO{" +
            "id=" + getId() +
            ", workOrderId=" + getWorkOrderId() +
            ", date='" + getDate() + "'" +
            ", params='" + getParams() + "'" +
            "}";
    }
}
