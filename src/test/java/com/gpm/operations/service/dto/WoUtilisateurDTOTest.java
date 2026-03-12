package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WoUtilisateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WoUtilisateurDTO.class);
        WoUtilisateurDTO woUtilisateurDTO1 = new WoUtilisateurDTO();
        woUtilisateurDTO1.setId(1L);
        WoUtilisateurDTO woUtilisateurDTO2 = new WoUtilisateurDTO();
        assertThat(woUtilisateurDTO1).isNotEqualTo(woUtilisateurDTO2);
        woUtilisateurDTO2.setId(woUtilisateurDTO1.getId());
        assertThat(woUtilisateurDTO1).isEqualTo(woUtilisateurDTO2);
        woUtilisateurDTO2.setId(2L);
        assertThat(woUtilisateurDTO1).isNotEqualTo(woUtilisateurDTO2);
        woUtilisateurDTO1.setId(null);
        assertThat(woUtilisateurDTO1).isNotEqualTo(woUtilisateurDTO2);
    }
}
