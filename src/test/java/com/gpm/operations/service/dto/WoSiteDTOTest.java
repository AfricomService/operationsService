package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WoSiteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WoSiteDTO.class);
        WoSiteDTO woSiteDTO1 = new WoSiteDTO();
        woSiteDTO1.setId(1L);
        WoSiteDTO woSiteDTO2 = new WoSiteDTO();
        assertThat(woSiteDTO1).isNotEqualTo(woSiteDTO2);
        woSiteDTO2.setId(woSiteDTO1.getId());
        assertThat(woSiteDTO1).isEqualTo(woSiteDTO2);
        woSiteDTO2.setId(2L);
        assertThat(woSiteDTO1).isNotEqualTo(woSiteDTO2);
        woSiteDTO1.setId(null);
        assertThat(woSiteDTO1).isNotEqualTo(woSiteDTO2);
    }
}
