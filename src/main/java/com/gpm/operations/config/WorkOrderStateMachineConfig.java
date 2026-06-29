package com.gpm.operations.config;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.domain.enumeration.WorkOrderEvent;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

@Configuration
@EnableStateMachineFactory
public class WorkOrderStateMachineConfig extends StateMachineConfigurerAdapter<StatutWO, WorkOrderEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<StatutWO, WorkOrderEvent> states) throws Exception {
        states.withStates().initial(StatutWO.Creation).states(EnumSet.allOf(StatutWO.class)).end(StatutWO.Fin);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<StatutWO, WorkOrderEvent> transitions) throws Exception {
        transitions
            .withExternal()
            .source(StatutWO.Creation)
            .target(StatutWO.ExecutionTravaux)
            .event(WorkOrderEvent.START_EXECUTION)
            .guard(checkVehicleAssignedAndSetTime())
            .action(setDateHeureDebutReel())
            .and()
            .withExternal()
            .source(StatutWO.ExecutionTravaux)
            .target(StatutWO.VerificationWO)
            .event(WorkOrderEvent.VERIFY)
            .and()
            .withExternal()
            .source(StatutWO.VerificationWO)
            .target(StatutWO.ValidationTechnique)
            .event(WorkOrderEvent.VALIDATE_TECH)
            .and()
            .withExternal()
            .source(StatutWO.ValidationTechnique)
            .target(StatutWO.ValidationRessources)
            .event(WorkOrderEvent.VALIDATE_RESOURCES)
            .and()
            .withExternal()
            .source(StatutWO.ValidationRessources)
            .target(StatutWO.Fin)
            .event(WorkOrderEvent.FINISH);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<StatutWO, WorkOrderEvent> config) throws Exception {
        config.withConfiguration().autoStartup(false);
    }

    private Guard<StatutWO, WorkOrderEvent> checkVehicleAssignedAndSetTime() {
        return context -> {
            WorkOrder wo = context.getMessageHeaders().get("WORK_ORDER", WorkOrder.class);
            return wo != null && wo.getVehiculeId() != null;
        };
    }

    private Action<StatutWO, WorkOrderEvent> setDateHeureDebutReel() {
        return context -> {
            WorkOrder wo = context.getMessageHeaders().get("WORK_ORDER", WorkOrder.class);
            if (wo != null) {
                wo.setDateHeureDebutReel(ZonedDateTime.now());
            }
        };
    }
}
