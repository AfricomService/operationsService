package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametrageDeplacementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParametrageDeplacementDTO.class);
        ParametrageDeplacementDTO parametrageDeplacementDTO1 = new ParametrageDeplacementDTO();
        parametrageDeplacementDTO1.setId(1L);
        ParametrageDeplacementDTO parametrageDeplacementDTO2 = new ParametrageDeplacementDTO();
        assertThat(parametrageDeplacementDTO1).isNotEqualTo(parametrageDeplacementDTO2);
        parametrageDeplacementDTO2.setId(parametrageDeplacementDTO1.getId());
        assertThat(parametrageDeplacementDTO1).isEqualTo(parametrageDeplacementDTO2);
        parametrageDeplacementDTO2.setId(2L);
        assertThat(parametrageDeplacementDTO1).isNotEqualTo(parametrageDeplacementDTO2);
        parametrageDeplacementDTO1.setId(null);
        assertThat(parametrageDeplacementDTO1).isNotEqualTo(parametrageDeplacementDTO2);
    }
}
