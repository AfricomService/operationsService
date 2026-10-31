package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParametrageDeplacementMapperTest {

    private ParametrageDeplacementMapper parametrageDeplacementMapper;

    @BeforeEach
    public void setUp() {
        parametrageDeplacementMapper = new ParametrageDeplacementMapperImpl();
    }
}
