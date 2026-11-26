package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NoteFraisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NoteFraisDTO.class);
        NoteFraisDTO noteFraisDTO1 = new NoteFraisDTO();
        noteFraisDTO1.setId(1L);
        NoteFraisDTO noteFraisDTO2 = new NoteFraisDTO();
        assertThat(noteFraisDTO1).isNotEqualTo(noteFraisDTO2);
        noteFraisDTO2.setId(noteFraisDTO1.getId());
        assertThat(noteFraisDTO1).isEqualTo(noteFraisDTO2);
        noteFraisDTO2.setId(2L);
        assertThat(noteFraisDTO1).isNotEqualTo(noteFraisDTO2);
        noteFraisDTO1.setId(null);
        assertThat(noteFraisDTO1).isNotEqualTo(noteFraisDTO2);
    }
}
