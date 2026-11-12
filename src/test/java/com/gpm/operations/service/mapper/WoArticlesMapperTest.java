package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WoArticlesMapperTest {

    private WoArticlesMapper woArticlesMapper;

    @BeforeEach
    public void setUp() {
        woArticlesMapper = new WoArticlesMapperImpl();
    }
}
