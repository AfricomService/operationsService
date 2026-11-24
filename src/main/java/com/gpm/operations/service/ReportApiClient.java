package com.gpm.operations.service;

import com.gpm.operations.service.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportApiClient {

    private final RestTemplate rt;

    public ReportApiClient(@Qualifier("reportRestTemplate") RestTemplate rt) {
        this.rt = rt;
    }

    /** GET /rapport-templates?page=0&size=20&withAccess=false */
    public PageResult<RemoteRapportTemplateDTO> getTemplates(int page, int size, boolean withAccess) {
        ResponseEntity<List<RemoteRapportTemplateDTO>> res = rt.exchange(
            "/rapport-templates?page={page}&size={size}&withAccess={withAccess}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<RemoteRapportTemplateDTO>>() {},
            page,
            size,
            withAccess
        );

        String totalHeader = res.getHeaders().getFirst("X-Total-Count");
        long total = totalHeader != null ? Long.parseLong(totalHeader) : 0L;
        return new PageResult<>(res.getBody(), total);
    }

    public RemoteRapportTemplateDTO getTemplate(Long id) {
        return rt.getForObject("/rapport-templates/{id}", RemoteRapportTemplateDTO.class, id);
    }

    public List<String> getEmails(Long id) {
        return rt
            .exchange("/rapport-templates/{id}/emails", HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {}, id)
            .getBody();
    }

    /** POST /rapport-templates/addEmail?rapportTemplateId=..&value=.. (request params, not a body) */
    public RemoteRapportTemplateDTO addEmail(Long templateId, String email) {
        URI uri = UriComponentsBuilder
            .fromPath("/rapport-templates/addEmail")
            .queryParam("rapportTemplateId", templateId)
            .queryParam("value", email)
            .build()
            .encode()
            .toUri();
        return rt.postForObject(uri, null, RemoteRapportTemplateDTO.class);
    }

    public RemoteRapportTemplateDTO create(RemoteRapportTemplateDTO dto) {
        return rt.postForObject("/rapport-templates", dto, RemoteRapportTemplateDTO.class);
    }

    public List<RemoteRapportTemplateDTO> findByTechAbility(boolean value) {
        return rt
            .exchange(
                "/rapport-templates/canBeCreatedByTech?canBeCreatedByTech={v}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RemoteRapportTemplateDTO>>() {},
                value
            )
            .getBody();
    }

    public byte[] downloadTemplateFile(Long id) {
        return rt.getForObject("/rapport-templates/{id}/template-file", byte[].class, id);
    }

    /** POST multipart /rapport-templates/uploadImportExcel */

    public void delete(Long id) {
        rt.delete("/rapport-templates/{id}", id);
    }

    /** POST /rapport-requests-custom?lineTypeList=1,2&filesDefList=3,4  (body = JSON DTO) */
    public RemoteRapportRequestDTO createCustom(RemoteRapportRequestDTO dto,
                                                List<Long> lineTypeIds,
                                                List<Long> filesDefIds) {
        String lineTypes = lineTypeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String filesDefs = filesDefIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        return rt.postForObject(
            "/rapport-requests-custom?lineTypeList={lt}&filesDefList={fd}",
            dto,
            RemoteRapportRequestDTO.class,
            lineTypes,
            filesDefs
        );
    }

    /** GET /line-types-by-template-id?rapportTemplateId=.. */
    public List<RemoteLineTypeDTO> getLineTypesByTemplate(Long templateId) {
        return rt.exchange(
            "/line-types-by-template-id?rapportTemplateId={id}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<RemoteLineTypeDTO>>() {},
            templateId
        ).getBody();
    }

    /** GET /files-definitions-by-rapport-template-id?rapportTemplateId=.. */
    public List<RemoteFilesDefinitionDTO> getFilesDefinitionsByTemplate(Long templateId) {
        return rt.exchange(
            "/files-definitions-by-rapport-template-id?rapportTemplateId={id}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<RemoteFilesDefinitionDTO>>() {},
            templateId
        ).getBody();
    }

    /** POST /rapport-requests/addAlternativeReportLineData?rapportRequestId=..&key=..&value=.. (request params, no body) */
    public RemoteRapportRequestDTO addAlternativeReportLineData(Long requestId, String key, String value) {
        return rt.postForObject(
            "/rapport-requests/addAlternativeReportLineData?rapportRequestId={id}&key={k}&value={v}",
            null,
            RemoteRapportRequestDTO.class,
            requestId,
            key,
            value
        );
    }

    /** GET /rapport-requests/by-alternative-reference?uniqueId=WO-6093-26 */
    public List<RemoteRapportRequestSummaryDTO> findByAlternativeReference(String uniqueId) {
        return rt.exchange(
            "/rapport-requests/by-alternative-reference?uniqueId={id}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<RemoteRapportRequestSummaryDTO>>() {},
            uniqueId
        ).getBody();
    }
}

