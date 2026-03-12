package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActiviteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActiviteDTO.class);
        ActiviteDTO activiteDTO1 = new ActiviteDTO();
        activiteDTO1.setId(1L);
        ActiviteDTO activiteDTO2 = new ActiviteDTO();
        assertThat(activiteDTO1).isNotEqualTo(activiteDTO2);
        activiteDTO2.setId(activiteDTO1.getId());
        assertThat(activiteDTO1).isEqualTo(activiteDTO2);
        activiteDTO2.setId(2L);
        assertThat(activiteDTO1).isNotEqualTo(activiteDTO2);
        activiteDTO1.setId(null);
        assertThat(activiteDTO1).isNotEqualTo(activiteDTO2);
    }
}
