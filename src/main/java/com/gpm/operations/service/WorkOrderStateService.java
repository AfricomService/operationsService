package com.gpm.operations.service;

import com.gpm.operations.domain.Mission;
import com.gpm.operations.domain.ParametrageDeplacement;
import com.gpm.operations.domain.ParametreWO;
import com.gpm.operations.domain.WorkOrder;
import com.gpm.operations.domain.enumeration.StatutWO;
import com.gpm.operations.domain.enumeration.WorkOrderEvent;
import com.gpm.operations.repository.MissionRepository;
import com.gpm.operations.repository.ParametrageDeplacementRepository;
import com.gpm.operations.repository.ParametreWORepository;
import com.gpm.operations.repository.WorkOrderRepository;
import com.gpm.operations.repository.WorkOrderTechniciensRepository;
import com.gpm.operations.security.SecurityUtils;
import com.gpm.operations.service.dto.MissionLineDTO;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkOrderStateService {

    // Titres de bénéficiaire hardcodés selon le rôle du contact sur le work order
    private static final String TITRE_MANAGER = "MANAGER";
    private static final String TITRE_TECHNIQUE = "TECHNIQUE";

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderTechniciensRepository workOrderTechniciensRepository;
    private final MissionRepository missionRepository;
    private final ParametrageDeplacementRepository parametrageDeplacementRepository;
    private final ParametreWORepository parametreWORepository;
    private final StateMachineFactory<StatutWO, WorkOrderEvent> stateMachineFactory;

    public WorkOrderStateService(
        WorkOrderRepository workOrderRepository,
        WorkOrderTechniciensRepository workOrderTechniciensRepository,
        MissionRepository missionRepository,
        ParametrageDeplacementRepository parametrageDeplacementRepository,
        ParametreWORepository parametreWORepository,
        StateMachineFactory<StatutWO, WorkOrderEvent> stateMachineFactory
    ) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderTechniciensRepository = workOrderTechniciensRepository;
        this.missionRepository = missionRepository;
        this.parametrageDeplacementRepository = parametrageDeplacementRepository;
        this.parametreWORepository = parametreWORepository;
        this.stateMachineFactory = stateMachineFactory;
    }

    /**
     * Démarre l'exécution : crée une mission par bénéficiaire (responsable + techniciens ENREGISTRÉS sur le work order)
     * puis déclenche START_EXECUTION. Tout est dans la même transaction : si la transition échoue, les missions sont annulées.
     */
    public StatutWO startExecution(Long workOrderId, List<MissionLineDTO> lines) {
        checkAuthorization(WorkOrderEvent.START_EXECUTION);

        WorkOrder workOrder = workOrderRepository
            .findById(workOrderId)
            .orElseThrow(() -> new IllegalArgumentException("WorkOrder not found: " + workOrderId));

        if (workOrder.getStatut() != StatutWO.Creation) {
            throw new IllegalStateException("Le work order n'est plus au statut Création.");
        }

        createMissions(workOrder, lines == null ? List.of() : lines);

        return changeStatus(workOrderId, WorkOrderEvent.START_EXECUTION);
    }

    private void createMissions(WorkOrder workOrder, List<MissionLineDTO> lines) {
        // Bénéficiaires attendus, d'après les données enregistrées en base : id du contact -> titre.
        // Le responsable est posé en premier : s'il est aussi technicien, il garde le titre MANAGER (une seule mission).
        Map<Long, String> expected = new LinkedHashMap<>();
        if (workOrder.getResponsableId() != null) {
            expected.put(workOrder.getResponsableId(), TITRE_MANAGER);
        }
        workOrderTechniciensRepository
            .findByWorkOrderId(workOrder.getId())
            .forEach(t -> {
                if (t.getContactSocieteId() != null) {
                    expected.putIfAbsent(t.getContactSocieteId(), TITRE_TECHNIQUE);
                }
            });

        Set<Long> received = lines.stream().map(MissionLineDTO::getContactSocieteId).collect(Collectors.toSet());

        if (received.size() != lines.size()) {
            throw new IllegalStateException("Un même bénéficiaire apparaît plusieurs fois dans la liste des missions.");
        }
        if (!received.equals(expected.keySet())) {
            throw new IllegalStateException(
                "La liste des missions ne correspond pas aux équipes enregistrées sur ce work order (responsable + techniciens). " +
                    "Enregistrez le work order (bouton Sauvegarder) puis réessayez."
            );
        }

        // Options cochées par jour (enregistrées en base) et montants par paramètre, chargés une seule fois
        List<ParametrageDeplacement> jours = parametrageDeplacementRepository.findByWorkOrderIdOrderByDateAsc(workOrder.getId());
        Map<String, ParametreWO> parametresParNom = parametreWORepository
            .findAll()
            .stream()
            .filter(p -> p.getNom() != null)
            .collect(Collectors.toMap(ParametreWO::getNom, p -> p, (a, b) -> a));

        for (MissionLineDTO line : lines) {
            String titre = expected.get(line.getContactSocieteId());

            Mission mission = new Mission();
            mission.setTitreBeneficiaire(titre);
            mission.setFraisPersonnel(calculerFraisPersonnel(jours, parametresParNom, TITRE_MANAGER.equals(titre)));
            mission.setMontantTotale(calculerMontantTotale(mission));
            mission.setNoteMission(line.getNoteMission());
            mission.setContactSocieteId(line.getContactSocieteId());
            mission.setContactSocieteMatricule(line.getContactSocieteMatricule());
            mission.setWorkorderId(workOrder.getId());
            missionRepository.save(mission);
        }
    }

    /**
     * Somme, pour chaque jour et chaque option cochée, du montant du paramètre :
     * montantChefTechnicien pour un MANAGER, montantTechnicien pour un TECHNIQUE.
     */
    private BigDecimal calculerFraisPersonnel(List<ParametrageDeplacement> jours, Map<String, ParametreWO> parametresParNom, boolean manager) {
        BigDecimal total = BigDecimal.ZERO;

        for (ParametrageDeplacement jour : jours) {
            if (jour.getParams() == null) {
                continue;
            }
            for (Map.Entry<String, Object> option : jour.getParams().entrySet()) {
                if (!Boolean.parseBoolean(String.valueOf(option.getValue()))) {
                    continue; // option non cochée ce jour-là
                }
                ParametreWO parametre = parametresParNom.get(option.getKey());
                if (parametre == null) {
                    continue;
                }
                BigDecimal montant = manager ? parametre.getMontantChefTechnicien() : parametre.getMontantTechnicien();
                if (montant != null) {
                    total = total.add(montant);
                }
            }
        }
        return total;
    }

    /** montantTotale = fraisJourFerie + fraisJourWeekend + fraisPersonnel (une valeur null compte pour 0). */
    private BigDecimal calculerMontantTotale(Mission mission) {
        return nullToZero(mission.getFraisJourFerie())
            .add(nullToZero(mission.getFraisJourWeekend()))
            .add(nullToZero(mission.getFraisPersonnel()));
    }

    private BigDecimal nullToZero(BigDecimal valeur) {
        return valeur != null ? valeur : BigDecimal.ZERO;
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
                throw new IllegalStateException("Transition « " + event + " » impossible depuis le statut " + previousState + ".");
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
        WorkOrderEvent.START_EXECUTION,
        new String[] { "ROLE_WO_EXECUTE", "ROLE_ADMIN" },
        WorkOrderEvent.VERIFY,
        new String[] { "ROLE_WO_VERIFY", "ROLE_ADMIN" },
        WorkOrderEvent.VALIDATE_TECH,
        new String[] { "ROLE_WO_VALIDATE_TECH", "ROLE_ADMIN" },
        WorkOrderEvent.VALIDATE_RESOURCES,
        new String[] { "ROLE_WO_VALIDATE_RESOURCES", "ROLE_ADMIN" },
        WorkOrderEvent.FINISH,
        new String[] { "ROLE_WO_FINISH", "ROLE_ADMIN" }
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
