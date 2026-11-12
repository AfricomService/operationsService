package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WoArticlesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WoArticlesDTO.class);
        WoArticlesDTO woArticlesDTO1 = new WoArticlesDTO();
        woArticlesDTO1.setId(1L);
        WoArticlesDTO woArticlesDTO2 = new WoArticlesDTO();
        assertThat(woArticlesDTO1).isNotEqualTo(woArticlesDTO2);
        woArticlesDTO2.setId(woArticlesDTO1.getId());
        assertThat(woArticlesDTO1).isEqualTo(woArticlesDTO2);
        woArticlesDTO2.setId(2L);
        assertThat(woArticlesDTO1).isNotEqualTo(woArticlesDTO2);
        woArticlesDTO1.setId(null);
        assertThat(woArticlesDTO1).isNotEqualTo(woArticlesDTO2);
    }
}
