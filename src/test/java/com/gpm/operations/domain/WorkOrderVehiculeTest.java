package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrderVehiculeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrderVehicule.class);
        WorkOrderVehicule workOrderVehicule1 = new WorkOrderVehicule();
        workOrderVehicule1.setId(1L);
        WorkOrderVehicule workOrderVehicule2 = new WorkOrderVehicule();
        workOrderVehicule2.setId(workOrderVehicule1.getId());
        assertThat(workOrderVehicule1).isEqualTo(workOrderVehicule2);
        workOrderVehicule2.setId(2L);
        assertThat(workOrderVehicule1).isNotEqualTo(workOrderVehicule2);
        workOrderVehicule1.setId(null);
        assertThat(workOrderVehicule1).isNotEqualTo(workOrderVehicule2);
    }
}
