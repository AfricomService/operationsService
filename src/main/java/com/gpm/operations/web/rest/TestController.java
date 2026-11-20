package com.gpm.operations.web.rest;

import com.gpm.operations.service.GatewayApiClient;
import com.gpm.operations.service.ReportApiClient;
import com.gpm.operations.service.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final ReportApiClient reports;
    private final GatewayApiClient gateway;

    public TestController(ReportApiClient reports, GatewayApiClient gateway) {
        this.reports = reports;
        this.gateway = gateway;
    }

    @GetMapping("/templates")
    public List<RemoteRapportTemplateDTO> templates() {
        return reports.getTemplates(0, 20, false).getContent();
    }

    @GetMapping("/users")
    public List<RemoteUserDTO> users() {
        return gateway.getUsers(0, 20);
    }

    @PostMapping("/rapport-request")
    public RemoteRapportRequestDTO createRequest() {
        RemoteRapportRequestDTO dto = new RemoteRapportRequestDTO();
        dto.setReference("TEST-001");
        dto.setClientfinal("CLIENT TEST");
        dto.setTechnicianId(6057L);          // real remote user id
        dto.setRapportTemplateId(1001L);       // real template id
        return reports.createCustom(dto, List.of(1672L, 1658L), List.of(953001L));
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
