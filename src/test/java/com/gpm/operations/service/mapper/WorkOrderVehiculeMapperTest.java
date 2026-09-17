package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkOrderVehiculeMapperTest {

    private WorkOrderVehiculeMapper workOrderVehiculeMapper;

    @BeforeEach
    public void setUp() {
        workOrderVehiculeMapper = new WorkOrderVehiculeMapperImpl();
    }
}
