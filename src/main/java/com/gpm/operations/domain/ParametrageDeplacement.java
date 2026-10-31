package com.gpm.operations.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.Map;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * A ParametrageDeplacement.
 */
@Entity
@Table(name = "parametrage_deplacement")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ParametrageDeplacement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "work_order_id")
    private Long workOrderId;

    @Column(name = "date")
    private ZonedDateTime date;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "params")
    private Map<String, Object> params;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ParametrageDeplacement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkOrderId() {
        return this.workOrderId;
    }

    public ParametrageDeplacement workOrderId(Long workOrderId) {
        this.setWorkOrderId(workOrderId);
        return this;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public ParametrageDeplacement date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public ParametrageDeplacement params(Map<String, Object> params) {
        this.setParams(params);
        return this;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParametrageDeplacement)) {
            return false;
        }
        return id != null && id.equals(((ParametrageDeplacement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametrageDeplacement{" +
            "id=" + getId() +
            ", workOrderId=" + getWorkOrderId() +
            ", date='" + getDate() + "'" +
            ", params='" + getParams() + "'" +
            "}";
    }
}
