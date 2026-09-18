package com.gpm.operations.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkOrderTechniciens.
 */
@Entity
@Table(name = "work_order_techniciens")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkOrderTechniciens implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "work_order_id")
    private Long workOrderId;

    @Column(name = "contact_societe_id")
    private Long contactSocieteId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkOrderTechniciens id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkOrderId() {
        return this.workOrderId;
    }

    public WorkOrderTechniciens workOrderId(Long workOrderId) {
        this.setWorkOrderId(workOrderId);
        return this;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getContactSocieteId() {
        return this.contactSocieteId;
    }

    public WorkOrderTechniciens contactSocieteId(Long contactSocieteId) {
        this.setContactSocieteId(contactSocieteId);
        return this;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkOrderTechniciens)) {
            return false;
        }
        return id != null && id.equals(((WorkOrderTechniciens) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkOrderTechniciens{" +
            "id=" + getId() +
            ", workOrderId=" + getWorkOrderId() +
            ", contactSocieteId=" + getContactSocieteId() +
            "}";
    }
}
