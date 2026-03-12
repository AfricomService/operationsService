package com.gpm.operations.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WoSite.
 */
@Entity
@Table(name = "wo_site")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WoSite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Cross-service FK → Site (projectService)
     */
    @Column(name = "site_code")
    private String siteCode;

    @ManyToOne(optional = false)
    @NotNull
    private WorkOrder workOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WoSite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteCode() {
        return this.siteCode;
    }

    public WoSite siteCode(String siteCode) {
        this.setSiteCode(siteCode);
        return this;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public WorkOrder getWorkOrder() {
        return this.workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public WoSite workOrder(WorkOrder workOrder) {
        this.setWorkOrder(workOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WoSite)) {
            return false;
        }
        return id != null && id.equals(((WoSite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WoSite{" +
            "id=" + getId() +
            ", siteCode='" + getSiteCode() + "'" +
            "}";
    }
}
