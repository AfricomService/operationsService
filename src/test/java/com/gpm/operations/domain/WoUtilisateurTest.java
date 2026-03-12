package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WoUtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WoUtilisateur.class);
        WoUtilisateur woUtilisateur1 = new WoUtilisateur();
        woUtilisateur1.setId(1L);
        WoUtilisateur woUtilisateur2 = new WoUtilisateur();
        woUtilisateur2.setId(woUtilisateur1.getId());
        assertThat(woUtilisateur1).isEqualTo(woUtilisateur2);
        woUtilisateur2.setId(2L);
        assertThat(woUtilisateur1).isNotEqualTo(woUtilisateur2);
        woUtilisateur1.setId(null);
        assertThat(woUtilisateur1).isNotEqualTo(woUtilisateur2);
    }
}
