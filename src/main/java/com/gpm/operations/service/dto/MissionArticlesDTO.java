package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.operations.domain.MissionArticles} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MissionArticlesDTO implements Serializable {

    private Long id;

    private Long woId;

    private Long articleId;

    private BigDecimal prixPropose;

    private ZonedDateTime dateAffectation;

    private Integer qte;

    private Integer quantitePlanifiee;

    private Integer quantiteRealisee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public BigDecimal getPrixPropose() {
        return prixPropose;
    }

    public void setPrixPropose(BigDecimal prixPropose) {
        this.prixPropose = prixPropose;
    }

    public ZonedDateTime getDateAffectation() {
        return dateAffectation;
    }

    public void setDateAffectation(ZonedDateTime dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public Integer getQte() {
        return qte;
    }

    public void setQte(Integer qte) {
        this.qte = qte;
    }

    public Integer getQuantitePlanifiee() {
        return quantitePlanifiee;
    }

    public void setQuantitePlanifiee(Integer quantitePlanifiee) {
        this.quantitePlanifiee = quantitePlanifiee;
    }

    public Integer getQuantiteRealisee() {
        return quantiteRealisee;
    }

    public void setQuantiteRealisee(Integer quantiteRealisee) {
        this.quantiteRealisee = quantiteRealisee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MissionArticlesDTO)) {
            return false;
        }

        MissionArticlesDTO missionArticlesDTO = (MissionArticlesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, missionArticlesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MissionArticlesDTO{" +
            "id=" + getId() +
            ", woId=" + getWoId() +
            ", articleId=" + getArticleId() +
            ", prixPropose=" + getPrixPropose() +
            ", dateAffectation='" + getDateAffectation() + "'" +
            ", qte=" + getQte() +
            ", quantitePlanifiee=" + getQuantitePlanifiee() +
            ", quantiteRealisee=" + getQuantiteRealisee() +
            "}";
    }
}
