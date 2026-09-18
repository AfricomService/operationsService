package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrderRessourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrderRessourceDTO.class);
        WorkOrderRessourceDTO workOrderRessourceDTO1 = new WorkOrderRessourceDTO();
        workOrderRessourceDTO1.setId(1L);
        WorkOrderRessourceDTO workOrderRessourceDTO2 = new WorkOrderRessourceDTO();
        assertThat(workOrderRessourceDTO1).isNotEqualTo(workOrderRessourceDTO2);
        workOrderRessourceDTO2.setId(workOrderRessourceDTO1.getId());
        assertThat(workOrderRessourceDTO1).isEqualTo(workOrderRessourceDTO2);
        workOrderRessourceDTO2.setId(2L);
        assertThat(workOrderRessourceDTO1).isNotEqualTo(workOrderRessourceDTO2);
        workOrderRessourceDTO1.setId(null);
        assertThat(workOrderRessourceDTO1).isNotEqualTo(workOrderRessourceDTO2);
    }
}
