package com.gpm.operations.domain;

import com.gpm.operations.domain.enumeration.StatutWO;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HistoriqueStatutWO.
 */
@Entity
@Table(name = "historique_statut_wo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueStatutWO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ancien_statut", nullable = false)
    private StatutWO ancienStatut;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nouveau_statut", nullable = false)
    private StatutWO nouveauStatut;

    @NotNull
    @Column(name = "date_changement", nullable = false)
    private ZonedDateTime dateChangement;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_login")
    private String userLogin;

    @ManyToOne(optional = false)
    @NotNull
    private WorkOrder workOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriqueStatutWO id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutWO getAncienStatut() {
        return this.ancienStatut;
    }

    public HistoriqueStatutWO ancienStatut(StatutWO ancienStatut) {
        this.setAncienStatut(ancienStatut);
        return this;
    }

    public void setAncienStatut(StatutWO ancienStatut) {
        this.ancienStatut = ancienStatut;
    }

    public StatutWO getNouveauStatut() {
        return this.nouveauStatut;
    }

    public HistoriqueStatutWO nouveauStatut(StatutWO nouveauStatut) {
        this.setNouveauStatut(nouveauStatut);
        return this;
    }

    public void setNouveauStatut(StatutWO nouveauStatut) {
        this.nouveauStatut = nouveauStatut;
    }

    public ZonedDateTime getDateChangement() {
        return this.dateChangement;
    }

    public HistoriqueStatutWO dateChangement(ZonedDateTime dateChangement) {
        this.setDateChangement(dateChangement);
        return this;
    }

    public void setDateChangement(ZonedDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public HistoriqueStatutWO commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getUserId() {
        return this.userId;
    }

    public HistoriqueStatutWO userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public HistoriqueStatutWO userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public WorkOrder getWorkOrder() {
        return this.workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public HistoriqueStatutWO workOrder(WorkOrder workOrder) {
        this.setWorkOrder(workOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueStatutWO)) {
            return false;
        }
        return id != null && id.equals(((HistoriqueStatutWO) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueStatutWO{" +
            "id=" + getId() +
            ", ancienStatut='" + getAncienStatut() + "'" +
            ", nouveauStatut='" + getNouveauStatut() + "'" +
            ", dateChangement='" + getDateChangement() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
