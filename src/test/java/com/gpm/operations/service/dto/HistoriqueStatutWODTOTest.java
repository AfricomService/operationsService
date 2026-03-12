package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriqueStatutWODTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueStatutWODTO.class);
        HistoriqueStatutWODTO historiqueStatutWODTO1 = new HistoriqueStatutWODTO();
        historiqueStatutWODTO1.setId(1L);
        HistoriqueStatutWODTO historiqueStatutWODTO2 = new HistoriqueStatutWODTO();
        assertThat(historiqueStatutWODTO1).isNotEqualTo(historiqueStatutWODTO2);
        historiqueStatutWODTO2.setId(historiqueStatutWODTO1.getId());
        assertThat(historiqueStatutWODTO1).isEqualTo(historiqueStatutWODTO2);
        historiqueStatutWODTO2.setId(2L);
        assertThat(historiqueStatutWODTO1).isNotEqualTo(historiqueStatutWODTO2);
        historiqueStatutWODTO1.setId(null);
        assertThat(historiqueStatutWODTO1).isNotEqualTo(historiqueStatutWODTO2);
    }
}
