package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkOrderMapperTest {

    private WorkOrderMapper workOrderMapper;

    @BeforeEach
    public void setUp() {
        workOrderMapper = new WorkOrderMapperImpl();
    }
}
