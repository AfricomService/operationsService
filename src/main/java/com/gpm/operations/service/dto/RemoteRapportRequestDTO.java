package com.gpm.operations.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteRapportRequestDTO {

    private Long id;                 // response only, must be null on create
    private String reference;        // required, max 64
    private String clientfinal;      // required, max 64
    private Long technicianId;
    private Long rapportTemplateId;  // see note below
    private Long statusId;           // response only

    private Object alternativeReportLineData;

    public Object getAlternativeReportLineData() {
        return alternativeReportLineData;
    }

    public void setAlternativeReportLineData(Object alternativeReportLineData) {
        this.alternativeReportLineData = alternativeReportLineData;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getClientfinal() { return clientfinal; }
    public void setClientfinal(String clientfinal) { this.clientfinal = clientfinal; }

    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public Long getRapportTemplateId() { return rapportTemplateId; }
    public void setRapportTemplateId(Long rapportTemplateId) { this.rapportTemplateId = rapportTemplateId; }

    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }
}
