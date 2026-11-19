package com.gpm.operations.service;

import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.domain.enumeration.WorkOrderEvent;
import com.gpm.operations.repository.WorkOrderRepository;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
import com.gpm.operations.security.SecurityUtils;

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
        checkAuthorization(event);

        WorkOrder workOrder = workOrderRepository
            .findById(workOrderId)
            .orElseThrow(() -> new IllegalArgumentException("WorkOrder not found: " + workOrderId));

        StatutWO previousState = workOrder.getStatut();
        StateMachine<StatutWO, WorkOrderEvent> sm = build(workOrder);

        try {
            boolean accepted = sm.sendEvent(MessageBuilder.withPayload(event).setHeader("WORK_ORDER", workOrder).build());
            // Après FINISH, la machine atteint l'état final et peut se terminer : on évite un NPE sur getState()
            StatutWO newState = sm.getState() != null ? sm.getState().getId() : (sm.isComplete() ? StatutWO.Fin : previousState);

            // Transition inexistante depuis ce statut, ou garde refusée : l'état n'a pas bougé
            if (!accepted || newState == previousState) {
                throw new IllegalStateException(
                    "Transition « " + event + " » impossible depuis le statut " + previousState + "."
                );
            }

            workOrder.setStatut(newState);
            workOrderRepository.save(workOrder);

            return newState;
        } finally {
            sm.stop();
        }
    }

    // Rôle requis pour chaque transition (noms = rôles du realm Keycloak Africom).
    // Un événement absent de la map serait ouvert à tout utilisateur authentifié : les 5 sont donc listés.
    private static final Map<WorkOrderEvent, String[]> REQUIRED_AUTHORITIES = Map.of(
        WorkOrderEvent.START_EXECUTION, new String[] { "ROLE_WO_EXECUTE", "ROLE_ADMIN" },
        WorkOrderEvent.VERIFY, new String[] { "ROLE_WO_VERIFY", "ROLE_ADMIN" },
        WorkOrderEvent.VALIDATE_TECH, new String[] { "ROLE_WO_VALIDATE_TECH", "ROLE_ADMIN" },
        WorkOrderEvent.VALIDATE_RESOURCES, new String[] { "ROLE_WO_VALIDATE_RESOURCES", "ROLE_ADMIN" },
        WorkOrderEvent.FINISH, new String[] { "ROLE_WO_FINISH", "ROLE_ADMIN" }
    );

    private void checkAuthorization(WorkOrderEvent event) {
        String[] required = REQUIRED_AUTHORITIES.get(event);
        if (required != null && !SecurityUtils.hasCurrentUserAnyOfAuthorities(required)) {
            throw new AccessDeniedException("Vous n'avez pas le droit de déclencher l'événement " + event);
        }
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
