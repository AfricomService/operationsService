package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrderDTO.class);
        WorkOrderDTO workOrderDTO1 = new WorkOrderDTO();
        workOrderDTO1.setId(1L);
        WorkOrderDTO workOrderDTO2 = new WorkOrderDTO();
        assertThat(workOrderDTO1).isNotEqualTo(workOrderDTO2);
        workOrderDTO2.setId(workOrderDTO1.getId());
        assertThat(workOrderDTO1).isEqualTo(workOrderDTO2);
        workOrderDTO2.setId(2L);
        assertThat(workOrderDTO1).isNotEqualTo(workOrderDTO2);
        workOrderDTO1.setId(null);
        assertThat(workOrderDTO1).isNotEqualTo(workOrderDTO2);
    }
}
