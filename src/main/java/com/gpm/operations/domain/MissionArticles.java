package com.gpm.operations.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MissionArticles.
 */
@Entity
@Table(name = "mission_articles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MissionArticles implements Serializable {

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

    @Column(name = "prix_propose", precision = 21, scale = 2)
    private BigDecimal prixPropose;

    @Column(name = "date_affectation")
    private ZonedDateTime dateAffectation;

    @Column(name = "qte")
    private Integer qte;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MissionArticles id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWoId() {
        return this.woId;
    }

    public MissionArticles woId(Long woId) {
        this.setWoId(woId);
        return this;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public Long getArticleId() {
        return this.articleId;
    }

    public MissionArticles articleId(Long articleId) {
        this.setArticleId(articleId);
        return this;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public BigDecimal getPrixPropose() {
        return this.prixPropose;
    }

    public MissionArticles prixPropose(BigDecimal prixPropose) {
        this.setPrixPropose(prixPropose);
        return this;
    }

    public void setPrixPropose(BigDecimal prixPropose) {
        this.prixPropose = prixPropose;
    }

    public ZonedDateTime getDateAffectation() {
        return this.dateAffectation;
    }

    public MissionArticles dateAffectation(ZonedDateTime dateAffectation) {
        this.setDateAffectation(dateAffectation);
        return this;
    }

    public void setDateAffectation(ZonedDateTime dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public Integer getQte() {
        return this.qte;
    }

    public MissionArticles qte(Integer qte) {
        this.setQte(qte);
        return this;
    }

    public void setQte(Integer qte) {
        this.qte = qte;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MissionArticles)) {
            return false;
        }
        return id != null && id.equals(((MissionArticles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MissionArticles{" +
            "id=" + getId() +
            ", woId=" + getWoId() +
            ", articleId=" + getArticleId() +
            ", prixPropose=" + getPrixPropose() +
            ", dateAffectation='" + getDateAffectation() + "'" +
            ", qte=" + getQte() +
            "}";
    }
}
