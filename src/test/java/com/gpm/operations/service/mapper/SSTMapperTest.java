package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SSTMapperTest {

    private SSTMapper sSTMapper;

    @BeforeEach
    public void setUp() {
        sSTMapper = new SSTMapperImpl();
    }
}
