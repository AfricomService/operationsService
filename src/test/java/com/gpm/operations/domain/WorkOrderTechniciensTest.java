package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrderTechniciensTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrderTechniciens.class);
        WorkOrderTechniciens workOrderTechniciens1 = new WorkOrderTechniciens();
        workOrderTechniciens1.setId(1L);
        WorkOrderTechniciens workOrderTechniciens2 = new WorkOrderTechniciens();
        workOrderTechniciens2.setId(workOrderTechniciens1.getId());
        assertThat(workOrderTechniciens1).isEqualTo(workOrderTechniciens2);
        workOrderTechniciens2.setId(2L);
        assertThat(workOrderTechniciens1).isNotEqualTo(workOrderTechniciens2);
        workOrderTechniciens1.setId(null);
        assertThat(workOrderTechniciens1).isNotEqualTo(workOrderTechniciens2);
    }
}
