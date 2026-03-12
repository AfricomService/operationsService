package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WoMotifDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WoMotifDTO.class);
        WoMotifDTO woMotifDTO1 = new WoMotifDTO();
        woMotifDTO1.setId(1L);
        WoMotifDTO woMotifDTO2 = new WoMotifDTO();
        assertThat(woMotifDTO1).isNotEqualTo(woMotifDTO2);
        woMotifDTO2.setId(woMotifDTO1.getId());
        assertThat(woMotifDTO1).isEqualTo(woMotifDTO2);
        woMotifDTO2.setId(2L);
        assertThat(woMotifDTO1).isNotEqualTo(woMotifDTO2);
        woMotifDTO1.setId(null);
        assertThat(woMotifDTO1).isNotEqualTo(woMotifDTO2);
    }
}
