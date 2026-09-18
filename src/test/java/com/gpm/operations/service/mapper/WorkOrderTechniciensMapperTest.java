package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkOrderTechniciensMapperTest {

    private WorkOrderTechniciensMapper workOrderTechniciensMapper;

    @BeforeEach
    public void setUp() {
        workOrderTechniciensMapper = new WorkOrderTechniciensMapperImpl();
    }
}
