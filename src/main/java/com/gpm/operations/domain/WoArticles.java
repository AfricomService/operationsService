package com.gpm.operations.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WoArticles.
 */
@Entity
@Table(name = "wo_articles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WoArticles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "wo_id")
    private Long woId;

    @Column(name = "article_id")
    private Long articleId;

    @Column(name = "date_realisation")
    private ZonedDateTime dateRealisation;

    @Column(name = "qte_commande")
    private Integer qteCommande;

    @Column(name = "qte_effectuee")
    private Integer qteEffectuee;

    @Column(name = "prix_article", precision = 21, scale = 2)
    private BigDecimal prixArticle;

    @Column(name = "prix_article_ht", precision = 21, scale = 2)
    private BigDecimal prixArticleHT;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WoArticles id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWoId() {
        return this.woId;
    }

    public WoArticles woId(Long woId) {
        this.setWoId(woId);
        return this;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public Long getArticleId() {
        return this.articleId;
    }

    public WoArticles articleId(Long articleId) {
        this.setArticleId(articleId);
        return this;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public ZonedDateTime getDateRealisation() {
        return this.dateRealisation;
    }

    public WoArticles dateRealisation(ZonedDateTime dateRealisation) {
        this.setDateRealisation(dateRealisation);
        return this;
    }

    public void setDateRealisation(ZonedDateTime dateRealisation) {
        this.dateRealisation = dateRealisation;
    }

    public Integer getQteCommande() {
        return this.qteCommande;
    }

    public WoArticles qteCommande(Integer qteCommande) {
        this.setQteCommande(qteCommande);
        return this;
    }

    public void setQteCommande(Integer qteCommande) {
        this.qteCommande = qteCommande;
    }

    public Integer getQteEffectuee() {
        return this.qteEffectuee;
    }

    public WoArticles qteEffectuee(Integer qteEffectuee) {
        this.setQteEffectuee(qteEffectuee);
        return this;
    }

    public void setQteEffectuee(Integer qteEffectuee) {
        this.qteEffectuee = qteEffectuee;
    }

    public BigDecimal getPrixArticle() {
        return this.prixArticle;
    }

    public WoArticles prixArticle(BigDecimal prixArticle) {
        this.setPrixArticle(prixArticle);
        return this;
    }

    public void setPrixArticle(BigDecimal prixArticle) {
        this.prixArticle = prixArticle;
    }

    public BigDecimal getPrixArticleHT() {
        return this.prixArticleHT;
    }

    public WoArticles prixArticleHT(BigDecimal prixArticleHT) {
        this.setPrixArticleHT(prixArticleHT);
        return this;
    }

    public void setPrixArticleHT(BigDecimal prixArticleHT) {
        this.prixArticleHT = prixArticleHT;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WoArticles)) {
            return false;
        }
        return id != null && id.equals(((WoArticles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WoArticles{" +
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
