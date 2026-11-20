package com.gpm.operations.web.rest;

import com.gpm.operations.service.GatewayApiClient;
import com.gpm.operations.service.ReportApiClient;
import com.gpm.operations.service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.client.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/test")
public class TestController {

    private final ReportApiClient reports;
    private final GatewayApiClient gateway;
    private static final Logger log = LoggerFactory.getLogger(TestController.class);


    public TestController(ReportApiClient reports, GatewayApiClient gateway) {
        this.reports = reports;
        this.gateway = gateway;
    }

    @GetMapping("/templates")
    public List<RemoteRapportTemplateDTO> templates(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "200") int size
    ) {
        return reports.getTemplates(page, size, false).getContent().stream()
            .filter(t -> !Boolean.FALSE.equals(t.getActif()))   // hide inactive templates
            .collect(Collectors.toList());
    }

    @GetMapping("/users")
    public List<RemoteUserDTO> users(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "200") int size
    ) {
        return gateway.getUsers(page, size).stream()
            .filter(RemoteUserDTO::isActivated)
            .collect(Collectors.toList());
    }

    @PostMapping("/rapport-request")
    public RemoteRapportRequestDTO createRequest(
        @RequestParam String reference,
        @RequestParam String clientFinal,
        @RequestParam Long technicianId,
        @RequestParam Long rapportTemplateId,
        @RequestParam List<Long> lineTypeIds,
        @RequestParam List<Long> filesDefIds
    ) {
        if (reference.length() > 64 || clientFinal.length() > 64) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reference and clientFinal must be at most 64 characters");
        }

        RemoteRapportRequestDTO dto = new RemoteRapportRequestDTO();
        dto.setReference(reference);
        dto.setClientfinal(clientFinal);
        dto.setTechnicianId(technicianId);
        dto.setRapportTemplateId(rapportTemplateId);

        // 1. create the ticket
        RemoteRapportRequestDTO created = reports.createCustom(dto, lineTypeIds, filesDefIds);

        // 2. attach the work order reference as an alternative report line
        if (created == null || created.getId() == null) {
            return created;
        }

        String woReference = stripExePrefix(reference);
        try {
            RemoteRapportRequestDTO updated =
                reports.addAlternativeReportLineData(created.getId(), "REFERENCE WO", woReference);
            return updated != null ? updated : created;
        } catch (RestClientException e) {
            // the ticket already exists remotely: don't fail the whole call, just log it
            log.warn("Ticket {} created but REFERENCE WO could not be added: {}", created.getId(), e.getMessage());
            return created;
        }
    }

    /** "EXE-WO-0020-26" (or "EXEWO-0020-26") -> "WO-0020-26" */
    private static String stripExePrefix(String reference) {
        if (reference.startsWith("EXE-")) {
            return reference.substring(4);
        }
        if (reference.startsWith("EXE")) {
            return reference.substring(3);
        }
        return reference;
    }

    @GetMapping("/line-types")
    public List<RemoteLineTypeDTO> lineTypes(@RequestParam Long templateId) {
        return reports.getLineTypesByTemplate(templateId);
    }

    @GetMapping("/files-definitions")
    public List<RemoteFilesDefinitionDTO> filesDefinitions(@RequestParam Long templateId) {
        return reports.getFilesDefinitionsByTemplate(templateId);
    }

    @PostMapping("/alternative-line-data")
    public RemoteRapportRequestDTO alternative(@RequestParam Long requestId,
                                               @RequestParam String key,
                                               @RequestParam String value) {
        return reports.addAlternativeReportLineData(requestId, key, value);
    }
}
