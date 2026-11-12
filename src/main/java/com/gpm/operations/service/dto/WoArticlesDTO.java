package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.operations.domain.WoArticles} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WoArticlesDTO implements Serializable {

    private Long id;

    private Long woId;

    private Long articleId;

    private ZonedDateTime dateRealisation;

    private Integer qteCommande;

    private Integer qteEffectuee;

    private BigDecimal prixArticle;

    private BigDecimal prixArticleHT;

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

    public ZonedDateTime getDateRealisation() {
        return dateRealisation;
    }

    public void setDateRealisation(ZonedDateTime dateRealisation) {
        this.dateRealisation = dateRealisation;
    }

    public Integer getQteCommande() {
        return qteCommande;
    }

    public void setQteCommande(Integer qteCommande) {
        this.qteCommande = qteCommande;
    }

    public Integer getQteEffectuee() {
        return qteEffectuee;
    }

    public void setQteEffectuee(Integer qteEffectuee) {
        this.qteEffectuee = qteEffectuee;
    }

    public BigDecimal getPrixArticle() {
        return prixArticle;
    }

    public void setPrixArticle(BigDecimal prixArticle) {
        this.prixArticle = prixArticle;
    }

    public BigDecimal getPrixArticleHT() {
        return prixArticleHT;
    }

    public void setPrixArticleHT(BigDecimal prixArticleHT) {
        this.prixArticleHT = prixArticleHT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WoArticlesDTO)) {
            return false;
        }

        WoArticlesDTO woArticlesDTO = (WoArticlesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, woArticlesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WoArticlesDTO{" +
            "id=" + getId() +
            ", woId=" + getWoId() +
            ", articleId=" + getArticleId() +
            ", dateRealisation='" + getDateRealisation() + "'" +
            ", qteCommande=" + getQteCommande() +
            ", qteEffectuee=" + getQteEffectuee() +
            ", prixArticle=" + getPrixArticle() +
            ", prixArticleHT=" + getPrixArticleHT() +
            "}";
    }
}
