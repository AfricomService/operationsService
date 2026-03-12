package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrder.class);
        WorkOrder workOrder1 = new WorkOrder();
        workOrder1.setId(1L);
        WorkOrder workOrder2 = new WorkOrder();
        workOrder2.setId(workOrder1.getId());
        assertThat(workOrder1).isEqualTo(workOrder2);
        workOrder2.setId(2L);
        assertThat(workOrder1).isNotEqualTo(workOrder2);
        workOrder1.setId(null);
        assertThat(workOrder1).isNotEqualTo(workOrder2);
    }
}
