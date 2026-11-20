package com.gpm.operations.web.rest;

import com.gpm.operations.service.GatewayApiClient;
import com.gpm.operations.service.ReportApiClient;
import com.gpm.operations.service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final ReportApiClient reports;
    private final GatewayApiClient gateway;

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
        // the remote side enforces @Size(max = 64) on both
        if (reference.length() > 64 || clientFinal.length() > 64) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reference and clientFinal must be at most 64 characters");
        }

        RemoteRapportRequestDTO dto = new RemoteRapportRequestDTO();
        dto.setReference(reference);
        dto.setClientfinal(clientFinal);
        dto.setTechnicianId(technicianId);
        dto.setRapportTemplateId(rapportTemplateId);
        return reports.createCustom(dto, lineTypeIds, filesDefIds);
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
