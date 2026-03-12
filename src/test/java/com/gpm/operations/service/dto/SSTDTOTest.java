package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SSTDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SSTDTO.class);
        SSTDTO sSTDTO1 = new SSTDTO();
        sSTDTO1.setId(1L);
        SSTDTO sSTDTO2 = new SSTDTO();
        assertThat(sSTDTO1).isNotEqualTo(sSTDTO2);
        sSTDTO2.setId(sSTDTO1.getId());
        assertThat(sSTDTO1).isEqualTo(sSTDTO2);
        sSTDTO2.setId(2L);
        assertThat(sSTDTO1).isNotEqualTo(sSTDTO2);
        sSTDTO1.setId(null);
        assertThat(sSTDTO1).isNotEqualTo(sSTDTO2);
    }
}
