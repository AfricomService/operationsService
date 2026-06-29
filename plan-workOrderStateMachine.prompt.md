## Plan: Spring State Machine for Work Orders

Introduce a Spring State Machine to strictly manage `WorkOrder` status transitions and enforce business validations.

### Steps

1. Add `spring-statemachine-core` dependency in pom.xml.
2. Create `WorkOrderEvent` enum in `com.gpm.operations.domain.enumeration` defining the requested event types (START_EXECUTION, VERIFY, VALIDATE_TECH, VALIDATE_RESOURCES, FINISH).
3. Create `WorkOrderStateMachineConfig` with `@Configuration` and `@EnableStateMachineFactory` defining transitions, mapping states from `StatutWO`, adding vehicle `Guard` (to prevent execution without a vehicle), and `Action` for setting `dateHeureDebutReel`.
4. Create new `WorkOrderStateService` class to load instances, recover state machine context, fire transition events, and persist entity updates. Handing of invalid transitions should throw exceptions.
5. Add a `POST /work-orders/{id}/transition/{event}` endpoint mapped in `WorkOrderResource` that integrates with `WorkOrderStateService` to process states and return the updated state.

### Further Considerations

1. Ensure the State Machine contextual integration works properly with the Hibernate entity manager to save entities seamlessly.
2. Dependency versioning: Ensure we use a compatible version of `spring-statemachine-core` with Spring Boot 2.7.3 (e.g., 3.2.0 or 3.2.1).
