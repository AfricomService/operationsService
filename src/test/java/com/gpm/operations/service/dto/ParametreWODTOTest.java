package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametreWODTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParametreWODTO.class);
        ParametreWODTO parametreWODTO1 = new ParametreWODTO();
        parametreWODTO1.setId(1L);
        ParametreWODTO parametreWODTO2 = new ParametreWODTO();
        assertThat(parametreWODTO1).isNotEqualTo(parametreWODTO2);
        parametreWODTO2.setId(parametreWODTO1.getId());
        assertThat(parametreWODTO1).isEqualTo(parametreWODTO2);
        parametreWODTO2.setId(2L);
        assertThat(parametreWODTO1).isNotEqualTo(parametreWODTO2);
        parametreWODTO1.setId(null);
        assertThat(parametreWODTO1).isNotEqualTo(parametreWODTO2);
    }
}
