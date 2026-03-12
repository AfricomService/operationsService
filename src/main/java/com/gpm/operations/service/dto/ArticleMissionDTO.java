package com.gpm.operations.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.operations.domain.ArticleMission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleMissionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer item;

    @NotNull
    private Float quantitePlanifiee;

    private Float quantiteRealisee;

    /**
     * Cross-service FK → Article (projectService)
     */
    @Schema(description = "Cross-service FK → Article (projectService)")
    private String articleCode;

    private WorkOrderDTO workOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Float getQuantitePlanifiee() {
        return quantitePlanifiee;
    }

    public void setQuantitePlanifiee(Float quantitePlanifiee) {
        this.quantitePlanifiee = quantitePlanifiee;
    }

    public Float getQuantiteRealisee() {
        return quantiteRealisee;
    }

    public void setQuantiteRealisee(Float quantiteRealisee) {
        this.quantiteRealisee = quantiteRealisee;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
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
        if (!(o instanceof ArticleMissionDTO)) {
            return false;
        }

        ArticleMissionDTO articleMissionDTO = (ArticleMissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, articleMissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleMissionDTO{" +
            "id=" + getId() +
            ", item=" + getItem() +
            ", quantitePlanifiee=" + getQuantitePlanifiee() +
            ", quantiteRealisee=" + getQuantiteRealisee() +
            ", articleCode='" + getArticleCode() + "'" +
            ", workOrder=" + getWorkOrder() +
            "}";
    }
}
