package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrderVehiculeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrderVehiculeDTO.class);
        WorkOrderVehiculeDTO workOrderVehiculeDTO1 = new WorkOrderVehiculeDTO();
        workOrderVehiculeDTO1.setId(1L);
        WorkOrderVehiculeDTO workOrderVehiculeDTO2 = new WorkOrderVehiculeDTO();
        assertThat(workOrderVehiculeDTO1).isNotEqualTo(workOrderVehiculeDTO2);
        workOrderVehiculeDTO2.setId(workOrderVehiculeDTO1.getId());
        assertThat(workOrderVehiculeDTO1).isEqualTo(workOrderVehiculeDTO2);
        workOrderVehiculeDTO2.setId(2L);
        assertThat(workOrderVehiculeDTO1).isNotEqualTo(workOrderVehiculeDTO2);
        workOrderVehiculeDTO1.setId(null);
        assertThat(workOrderVehiculeDTO1).isNotEqualTo(workOrderVehiculeDTO2);
    }
}
