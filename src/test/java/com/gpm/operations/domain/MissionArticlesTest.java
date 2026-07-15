package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MissionArticlesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MissionArticles.class);
        MissionArticles missionArticles1 = new MissionArticles();
        missionArticles1.setId(1L);
        MissionArticles missionArticles2 = new MissionArticles();
        missionArticles2.setId(missionArticles1.getId());
        assertThat(missionArticles1).isEqualTo(missionArticles2);
        missionArticles2.setId(2L);
        assertThat(missionArticles1).isNotEqualTo(missionArticles2);
        missionArticles1.setId(null);
        assertThat(missionArticles1).isNotEqualTo(missionArticles2);
    }
}
