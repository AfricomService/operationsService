package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriqueStatutWOMapperTest {

    private HistoriqueStatutWOMapper historiqueStatutWOMapper;

    @BeforeEach
    public void setUp() {
        historiqueStatutWOMapper = new HistoriqueStatutWOMapperImpl();
    }
}
