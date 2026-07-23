package com.gpm.operations.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.operations.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MissionArticlesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MissionArticlesDTO.class);
        MissionArticlesDTO missionArticlesDTO1 = new MissionArticlesDTO();
        missionArticlesDTO1.setId(1L);
        MissionArticlesDTO missionArticlesDTO2 = new MissionArticlesDTO();
        assertThat(missionArticlesDTO1).isNotEqualTo(missionArticlesDTO2);
        missionArticlesDTO2.setId(missionArticlesDTO1.getId());
        assertThat(missionArticlesDTO1).isEqualTo(missionArticlesDTO2);
        missionArticlesDTO2.setId(2L);
        assertThat(missionArticlesDTO1).isNotEqualTo(missionArticlesDTO2);
        missionArticlesDTO1.setId(null);
        assertThat(missionArticlesDTO1).isNotEqualTo(missionArticlesDTO2);
    }
}
