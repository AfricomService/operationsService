package com.gpm.operations.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.operations.domain.WoUtilisateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WoUtilisateurDTO implements Serializable {

    private Long id;

    /**
     * ResponsableMission | Coordinateur | Membre
     */
    @NotNull
    @Schema(description = "ResponsableMission | Coordinateur | Membre", required = true)
    private String role;

    private String userId;

    private String userLogin;

    private WorkOrderDTO workOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public WorkOrderDTO getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrderDTO workOrder) {
        this.workOrder = workOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WoUtilisateurDTO)) {
            return false;
        }

        WoUtilisateurDTO woUtilisateurDTO = (WoUtilisateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, woUtilisateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WoUtilisateurDTO{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", workOrder=" + getWorkOrder() +
            "}";
    }
}
