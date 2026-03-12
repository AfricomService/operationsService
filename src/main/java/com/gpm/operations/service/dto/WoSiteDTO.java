package com.gpm.operations.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.operations.domain.WoSite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WoSiteDTO implements Serializable {

    private Long id;

    /**
     * Cross-service FK → Site (projectService)
     */
    @Schema(description = "Cross-service FK → Site (projectService)")
    private String siteCode;

    private WorkOrderDTO workOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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
        if (!(o instanceof WoSiteDTO)) {
            return false;
        }

        WoSiteDTO woSiteDTO = (WoSiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, woSiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WoSiteDTO{" +
            "id=" + getId() +
            ", siteCode='" + getSiteCode() + "'" +
            ", workOrder=" + getWorkOrder() +
            "}";
    }
}
