package com.gpm.operations.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArticleMission.
 */
@Entity
@Table(name = "article_mission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleMission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "item", nullable = false)
    private Integer item;

    @NotNull
    @Column(name = "quantite_planifiee", nullable = false)
    private Float quantitePlanifiee;

    @Column(name = "quantite_realisee")
    private Float quantiteRealisee;

    /**
     * Cross-service FK → Article (projectService)
     */
    @Column(name = "article_code")
    private String articleCode;

    @ManyToOne(optional = false)
    @NotNull
    private WorkOrder workOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArticleMission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItem() {
        return this.item;
    }

    public ArticleMission item(Integer item) {
        this.setItem(item);
        return this;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Float getQuantitePlanifiee() {
        return this.quantitePlanifiee;
    }

    public ArticleMission quantitePlanifiee(Float quantitePlanifiee) {
        this.setQuantitePlanifiee(quantitePlanifiee);
        return this;
    }

    public void setQuantitePlanifiee(Float quantitePlanifiee) {
        this.quantitePlanifiee = quantitePlanifiee;
    }

    public Float getQuantiteRealisee() {
        return this.quantiteRealisee;
    }

    public ArticleMission quantiteRealisee(Float quantiteRealisee) {
        this.setQuantiteRealisee(quantiteRealisee);
        return this;
    }

    public void setQuantiteRealisee(Float quantiteRealisee) {
        this.quantiteRealisee = quantiteRealisee;
    }

    public String getArticleCode() {
        return this.articleCode;
    }

    public ArticleMission articleCode(String articleCode) {
        this.setArticleCode(articleCode);
        return this;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public WorkOrder getWorkOrder() {
        return this.workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public ArticleMission workOrder(WorkOrder workOrder) {
        this.setWorkOrder(workOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleMission)) {
            return false;
        }
        return id != null && id.equals(((ArticleMission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleMission{" +
            "id=" + getId() +
            ", item=" + getItem() +
            ", quantitePlanifiee=" + getQuantitePlanifiee() +
            ", quantiteRealisee=" + getQuantiteRealisee() +
            ", articleCode='" + getArticleCode() + "'" +
            "}";
    }
}
