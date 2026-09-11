package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrderTechniciensDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrderTechniciensDTO.class);
        WorkOrderTechniciensDTO workOrderTechniciensDTO1 = new WorkOrderTechniciensDTO();
        workOrderTechniciensDTO1.setId(1L);
        WorkOrderTechniciensDTO workOrderTechniciensDTO2 = new WorkOrderTechniciensDTO();
        assertThat(workOrderTechniciensDTO1).isNotEqualTo(workOrderTechniciensDTO2);
        workOrderTechniciensDTO2.setId(workOrderTechniciensDTO1.getId());
        assertThat(workOrderTechniciensDTO1).isEqualTo(workOrderTechniciensDTO2);
        workOrderTechniciensDTO2.setId(2L);
        assertThat(workOrderTechniciensDTO1).isNotEqualTo(workOrderTechniciensDTO2);
        workOrderTechniciensDTO1.setId(null);
        assertThat(workOrderTechniciensDTO1).isNotEqualTo(workOrderTechniciensDTO2);
    }
}
