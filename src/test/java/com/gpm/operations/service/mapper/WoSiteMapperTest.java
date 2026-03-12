package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WoSiteMapperTest {

    private WoSiteMapper woSiteMapper;

    @BeforeEach
    public void setUp() {
        woSiteMapper = new WoSiteMapperImpl();
    }
}
