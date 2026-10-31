package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametrageDeplacementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParametrageDeplacement.class);
        ParametrageDeplacement parametrageDeplacement1 = new ParametrageDeplacement();
        parametrageDeplacement1.setId(1L);
        ParametrageDeplacement parametrageDeplacement2 = new ParametrageDeplacement();
        parametrageDeplacement2.setId(parametrageDeplacement1.getId());
        assertThat(parametrageDeplacement1).isEqualTo(parametrageDeplacement2);
        parametrageDeplacement2.setId(2L);
        assertThat(parametrageDeplacement1).isNotEqualTo(parametrageDeplacement2);
        parametrageDeplacement1.setId(null);
        assertThat(parametrageDeplacement1).isNotEqualTo(parametrageDeplacement2);
    }
}
