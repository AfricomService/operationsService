package com.gpm.operations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleMissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleMission.class);
        ArticleMission articleMission1 = new ArticleMission();
        articleMission1.setId(1L);
        ArticleMission articleMission2 = new ArticleMission();
        articleMission2.setId(articleMission1.getId());
        assertThat(articleMission1).isEqualTo(articleMission2);
        articleMission2.setId(2L);
        assertThat(articleMission1).isNotEqualTo(articleMission2);
        articleMission1.setId(null);
        assertThat(articleMission1).isNotEqualTo(articleMission2);
    }
}
