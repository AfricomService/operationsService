package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WoArticlesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WoArticles.class);
        WoArticles woArticles1 = new WoArticles();
        woArticles1.setId(1L);
        WoArticles woArticles2 = new WoArticles();
        woArticles2.setId(woArticles1.getId());
        assertThat(woArticles1).isEqualTo(woArticles2);
        woArticles2.setId(2L);
        assertThat(woArticles1).isNotEqualTo(woArticles2);
        woArticles1.setId(null);
        assertThat(woArticles1).isNotEqualTo(woArticles2);
    }
}
