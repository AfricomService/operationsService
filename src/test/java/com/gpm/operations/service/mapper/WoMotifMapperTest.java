package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WoMotifMapperTest {

    private WoMotifMapper woMotifMapper;

    @BeforeEach
    public void setUp() {
        woMotifMapper = new WoMotifMapperImpl();
    }
}
