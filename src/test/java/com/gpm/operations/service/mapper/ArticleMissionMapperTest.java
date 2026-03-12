package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleMissionMapperTest {

    private ArticleMissionMapper articleMissionMapper;

    @BeforeEach
    public void setUp() {
        articleMissionMapper = new ArticleMissionMapperImpl();
    }
}
