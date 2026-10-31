package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametreWOTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParametreWO.class);
        ParametreWO parametreWO1 = new ParametreWO();
        parametreWO1.setId(1L);
        ParametreWO parametreWO2 = new ParametreWO();
        parametreWO2.setId(parametreWO1.getId());
        assertThat(parametreWO1).isEqualTo(parametreWO2);
        parametreWO2.setId(2L);
        assertThat(parametreWO1).isNotEqualTo(parametreWO2);
        parametreWO1.setId(null);
        assertThat(parametreWO1).isNotEqualTo(parametreWO2);
    }
}
