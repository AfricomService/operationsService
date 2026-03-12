package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WoSiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WoSite.class);
        WoSite woSite1 = new WoSite();
        woSite1.setId(1L);
        WoSite woSite2 = new WoSite();
        woSite2.setId(woSite1.getId());
        assertThat(woSite1).isEqualTo(woSite2);
        woSite2.setId(2L);
        assertThat(woSite1).isNotEqualTo(woSite2);
        woSite1.setId(null);
        assertThat(woSite1).isNotEqualTo(woSite2);
    }
}
