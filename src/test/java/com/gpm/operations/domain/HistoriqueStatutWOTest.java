package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriqueStatutWOTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueStatutWO.class);
        HistoriqueStatutWO historiqueStatutWO1 = new HistoriqueStatutWO();
        historiqueStatutWO1.setId(1L);
        HistoriqueStatutWO historiqueStatutWO2 = new HistoriqueStatutWO();
        historiqueStatutWO2.setId(historiqueStatutWO1.getId());
        assertThat(historiqueStatutWO1).isEqualTo(historiqueStatutWO2);
        historiqueStatutWO2.setId(2L);
        assertThat(historiqueStatutWO1).isNotEqualTo(historiqueStatutWO2);
        historiqueStatutWO1.setId(null);
        assertThat(historiqueStatutWO1).isNotEqualTo(historiqueStatutWO2);
    }
}
