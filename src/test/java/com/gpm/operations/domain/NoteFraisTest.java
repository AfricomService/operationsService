package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NoteFraisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NoteFrais.class);
        NoteFrais noteFrais1 = new NoteFrais();
        noteFrais1.setId(1L);
        NoteFrais noteFrais2 = new NoteFrais();
        noteFrais2.setId(noteFrais1.getId());
        assertThat(noteFrais1).isEqualTo(noteFrais2);
        noteFrais2.setId(2L);
        assertThat(noteFrais1).isNotEqualTo(noteFrais2);
        noteFrais1.setId(null);
        assertThat(noteFrais1).isNotEqualTo(noteFrais2);
    }
}
