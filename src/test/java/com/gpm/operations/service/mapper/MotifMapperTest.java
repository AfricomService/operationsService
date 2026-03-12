package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MotifMapperTest {

    private MotifMapper motifMapper;

    @BeforeEach
    public void setUp() {
        motifMapper = new MotifMapperImpl();
    }
}
