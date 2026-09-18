package com.gpm.operations.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkOrderRessourceMapperTest {

    private WorkOrderRessourceMapper workOrderRessourceMapper;

    @BeforeEach
    public void setUp() {
        workOrderRessourceMapper = new WorkOrderRessourceMapperImpl();
    }
}
