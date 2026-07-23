package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MissionArticlesMapperTest {

    private MissionArticlesMapper missionArticlesMapper;

    @BeforeEach
    public void setUp() {
        missionArticlesMapper = new MissionArticlesMapperImpl();
    }
}
