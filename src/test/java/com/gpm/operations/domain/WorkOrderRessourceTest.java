package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrderRessourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrderRessource.class);
        WorkOrderRessource workOrderRessource1 = new WorkOrderRessource();
        workOrderRessource1.setId(1L);
        WorkOrderRessource workOrderRessource2 = new WorkOrderRessource();
        workOrderRessource2.setId(workOrderRessource1.getId());
        assertThat(workOrderRessource1).isEqualTo(workOrderRessource2);
        workOrderRessource2.setId(2L);
        assertThat(workOrderRessource1).isNotEqualTo(workOrderRessource2);
        workOrderRessource1.setId(null);
        assertThat(workOrderRessource1).isNotEqualTo(workOrderRessource2);
    }
}
