package com.gpm.operations.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

/** Corps de POST /api/work-orders/{id}/start-execution. */
public class StartExecutionDTO implements Serializable {

    @Valid
    private List<MissionLineDTO> missions = new ArrayList<>();

    public List<MissionLineDTO> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionLineDTO> missions) {
        this.missions = missions;
    }
}
