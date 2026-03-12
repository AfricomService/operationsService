package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WoMotifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WoMotif.class);
        WoMotif woMotif1 = new WoMotif();
        woMotif1.setId(1L);
        WoMotif woMotif2 = new WoMotif();
        woMotif2.setId(woMotif1.getId());
        assertThat(woMotif1).isEqualTo(woMotif2);
        woMotif2.setId(2L);
        assertThat(woMotif1).isNotEqualTo(woMotif2);
        woMotif1.setId(null);
        assertThat(woMotif1).isNotEqualTo(woMotif2);
    }
}
