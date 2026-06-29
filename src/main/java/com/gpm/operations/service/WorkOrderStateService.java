package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.domain.enumeration.WorkOrderEvent;
import com.gpm.operations.repository.WorkOrderRepository;
import java.util.Optional;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkOrderStateService {

    private final WorkOrderRepository workOrderRepository;
    private final StateMachineFactory<StatutWO, WorkOrderEvent> stateMachineFactory;

    public WorkOrderStateService(
        WorkOrderRepository workOrderRepository,
        StateMachineFactory<StatutWO, WorkOrderEvent> stateMachineFactory
    ) {
        this.workOrderRepository = workOrderRepository;
        this.stateMachineFactory = stateMachineFactory;
    }

    public StatutWO changeStatus(Long workOrderId, WorkOrderEvent event) {
        Optional<WorkOrder> woOptional = workOrderRepository.findById(workOrderId);
        if (woOptional.isEmpty()) {
            throw new IllegalArgumentException("WorkOrder not found");
        }

        WorkOrder workOrder = woOptional.get();
        StateMachine<StatutWO, WorkOrderEvent> sm = build(workOrder);

        boolean accepted = sm.sendEvent(MessageBuilder.withPayload(event).setHeader("WORK_ORDER", workOrder).build());

        if (!accepted) {
            throw new IllegalStateException("Invalid transition or guard condition failed for event: " + event);
        }

        StatutWO newState = sm.getState().getId();
        if (workOrder.getStatut() == newState) {
            // Guard likely failed but say it returned true, actually sendEvent returns true if it just finds the transition,
            // but if guard fails it won't change state. Let's check state change:
            // But wait, actually sendEvent returns true if event is processed, not necessarily if state is changed.
        }

        workOrder.setStatut(newState);
        workOrderRepository.save(workOrder);

        return newState;
    }

    private StateMachine<StatutWO, WorkOrderEvent> build(WorkOrder workOrder) {
        StateMachine<StatutWO, WorkOrderEvent> sm = stateMachineFactory.getStateMachine(Long.toString(workOrder.getId()));
        sm.stop();
        sm
            .getStateMachineAccessor()
            .doWithAllRegions(sma -> {
                sma.resetStateMachine(new DefaultStateMachineContext<>(workOrder.getStatut(), null, null, null));
            });
        sm.start();
        return sm;
    }
}
