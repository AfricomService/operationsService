package com.gpm.operations.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkOrderRessource.
 */
@Entity
@Table(name = "work_order_ressource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkOrderRessource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "work_order_id")
    private Long workOrderId;

    @Column(name = "ressource_id")
    private Long ressourceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkOrderRessource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkOrderId() {
        return this.workOrderId;
    }

    public WorkOrderRessource workOrderId(Long workOrderId) {
        this.setWorkOrderId(workOrderId);
        return this;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getRessourceId() {
        return this.ressourceId;
    }

    public WorkOrderRessource ressourceId(Long ressourceId) {
        this.setRessourceId(ressourceId);
        return this;
    }

    public void setRessourceId(Long ressourceId) {
        this.ressourceId = ressourceId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkOrderRessource)) {
            return false;
        }
        return id != null && id.equals(((WorkOrderRessource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkOrderRessource{" +
            "id=" + getId() +
            ", workOrderId=" + getWorkOrderId() +
            ", ressourceId=" + getRessourceId() +
            "}";
    }
}
