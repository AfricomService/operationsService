package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActiviteMapperTest {

    private ActiviteMapper activiteMapper;

    @BeforeEach
    public void setUp() {
        activiteMapper = new ActiviteMapperImpl();
    }
}
