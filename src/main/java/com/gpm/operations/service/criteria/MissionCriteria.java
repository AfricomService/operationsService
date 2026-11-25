package com.gpm.operations.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.gpm.operations.domain.Mission} entity. This class is used
 * in {@link com.gpm.operations.web.rest.MissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /missions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MissionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titreBeneficiaire;

    private BigDecimalFilter noteMission;

    private LongFilter contactSocieteId;

    private StringFilter contactSocieteMatricule;

    private BigDecimalFilter fraisJourFerie;

    private BigDecimalFilter fraisJourWeekend;

    private BigDecimalFilter fraisPersonnel;

    private BigDecimalFilter montantTotale;

    private LongFilter workorderId;

    private LongFilter noteFraisId;

    private Boolean distinct;

    public MissionCriteria() {}

    public MissionCriteria(MissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titreBeneficiaire = other.titreBeneficiaire == null ? null : other.titreBeneficiaire.copy();
        this.noteMission = other.noteMission == null ? null : other.noteMission.copy();
        this.contactSocieteId = other.contactSocieteId == null ? null : other.contactSocieteId.copy();
        this.contactSocieteMatricule = other.contactSocieteMatricule == null ? null : other.contactSocieteMatricule.copy();
        this.fraisJourFerie = other.fraisJourFerie == null ? null : other.fraisJourFerie.copy();
        this.fraisJourWeekend = other.fraisJourWeekend == null ? null : other.fraisJourWeekend.copy();
        this.fraisPersonnel = other.fraisPersonnel == null ? null : other.fraisPersonnel.copy();
        this.montantTotale = other.montantTotale == null ? null : other.montantTotale.copy();
        this.workorderId = other.workorderId == null ? null : other.workorderId.copy();
        this.noteFraisId = other.noteFraisId == null ? null : other.noteFraisId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MissionCriteria copy() {
        return new MissionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitreBeneficiaire() {
        return titreBeneficiaire;
    }

    public StringFilter titreBeneficiaire() {
        if (titreBeneficiaire == null) {
            titreBeneficiaire = new StringFilter();
        }
        return titreBeneficiaire;
    }

    public void setTitreBeneficiaire(StringFilter titreBeneficiaire) {
        this.titreBeneficiaire = titreBeneficiaire;
    }

    public BigDecimalFilter getNoteMission() {
        return noteMission;
    }

    public BigDecimalFilter noteMission() {
        if (noteMission == null) {
            noteMission = new BigDecimalFilter();
        }
        return noteMission;
    }

    public void setNoteMission(BigDecimalFilter noteMission) {
        this.noteMission = noteMission;
    }

    public LongFilter getContactSocieteId() {
        return contactSocieteId;
    }

    public LongFilter contactSocieteId() {
        if (contactSocieteId == null) {
            contactSocieteId = new LongFilter();
        }
        return contactSocieteId;
    }

    public void setContactSocieteId(LongFilter contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    public StringFilter getContactSocieteMatricule() {
        return contactSocieteMatricule;
    }

    public StringFilter contactSocieteMatricule() {
        if (contactSocieteMatricule == null) {
            contactSocieteMatricule = new StringFilter();
        }
        return contactSocieteMatricule;
    }

    public void setContactSocieteMatricule(StringFilter contactSocieteMatricule) {
        this.contactSocieteMatricule = contactSocieteMatricule;
    }

    public BigDecimalFilter getFraisJourFerie() {
        return fraisJourFerie;
    }

    public BigDecimalFilter fraisJourFerie() {
        if (fraisJourFerie == null) {
            fraisJourFerie = new BigDecimalFilter();
        }
        return fraisJourFerie;
    }

    public void setFraisJourFerie(BigDecimalFilter fraisJourFerie) {
        this.fraisJourFerie = fraisJourFerie;
    }

    public BigDecimalFilter getFraisJourWeekend() {
        return fraisJourWeekend;
    }

    public BigDecimalFilter fraisJourWeekend() {
        if (fraisJourWeekend == null) {
            fraisJourWeekend = new BigDecimalFilter();
        }
        return fraisJourWeekend;
    }

    public void setFraisJourWeekend(BigDecimalFilter fraisJourWeekend) {
        this.fraisJourWeekend = fraisJourWeekend;
    }

    public BigDecimalFilter getFraisPersonnel() {
        return fraisPersonnel;
    }

    public BigDecimalFilter fraisPersonnel() {
        if (fraisPersonnel == null) {
            fraisPersonnel = new BigDecimalFilter();
        }
        return fraisPersonnel;
    }

    public void setFraisPersonnel(BigDecimalFilter fraisPersonnel) {
        this.fraisPersonnel = fraisPersonnel;
    }

    public BigDecimalFilter getMontantTotale() {
        return montantTotale;
    }

    public BigDecimalFilter montantTotale() {
        if (montantTotale == null) {
            montantTotale = new BigDecimalFilter();
        }
        return montantTotale;
    }

    public void setMontantTotale(BigDecimalFilter montantTotale) {
        this.montantTotale = montantTotale;
    }

    public LongFilter getWorkorderId() {
        return workorderId;
    }

    public LongFilter workorderId() {
        if (workorderId == null) {
            workorderId = new LongFilter();
        }
        return workorderId;
    }

    public void setWorkorderId(LongFilter workorderId) {
        this.workorderId = workorderId;
    }

    public LongFilter getNoteFraisId() {
        return noteFraisId;
    }

    public LongFilter noteFraisId() {
        if (noteFraisId == null) {
            noteFraisId = new LongFilter();
        }
        return noteFraisId;
    }

    public void setNoteFraisId(LongFilter noteFraisId) {
        this.noteFraisId = noteFraisId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MissionCriteria that = (MissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titreBeneficiaire, that.titreBeneficiaire) &&
            Objects.equals(noteMission, that.noteMission) &&
            Objects.equals(contactSocieteId, that.contactSocieteId) &&
            Objects.equals(contactSocieteMatricule, that.contactSocieteMatricule) &&
            Objects.equals(fraisJourFerie, that.fraisJourFerie) &&
            Objects.equals(fraisJourWeekend, that.fraisJourWeekend) &&
            Objects.equals(fraisPersonnel, that.fraisPersonnel) &&
            Objects.equals(montantTotale, that.montantTotale) &&
            Objects.equals(workorderId, that.workorderId) &&
            Objects.equals(noteFraisId, that.noteFraisId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            titreBeneficiaire,
            noteMission,
            contactSocieteId,
            contactSocieteMatricule,
            fraisJourFerie,
            fraisJourWeekend,
            fraisPersonnel,
            montantTotale,
            workorderId,
            noteFraisId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MissionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (titreBeneficiaire != null ? "titreBeneficiaire=" + titreBeneficiaire + ", " : "") +
            (noteMission != null ? "noteMission=" + noteMission + ", " : "") +
            (contactSocieteId != null ? "contactSocieteId=" + contactSocieteId + ", " : "") +
            (contactSocieteMatricule != null ? "contactSocieteMatricule=" + contactSocieteMatricule + ", " : "") +
            (fraisJourFerie != null ? "fraisJourFerie=" + fraisJourFerie + ", " : "") +
            (fraisJourWeekend != null ? "fraisJourWeekend=" + fraisJourWeekend + ", " : "") +
            (fraisPersonnel != null ? "fraisPersonnel=" + fraisPersonnel + ", " : "") +
            (montantTotale != null ? "montantTotale=" + montantTotale + ", " : "") +
            (workorderId != null ? "workorderId=" + workorderId + ", " : "") +
            (noteFraisId != null ? "noteFraisId=" + noteFraisId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
