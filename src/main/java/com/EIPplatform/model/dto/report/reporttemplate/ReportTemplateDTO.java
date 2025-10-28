package com.EIPplatform.model.dto.report.reporttemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTemplateDTO implements Serializable {
    private Integer reportTemplateId;
    private String templateCode;
    private String templateName;
    private Integer templateVersion;
    private String schemaDefinition;
    private Boolean isActive;
    private LocalDate effectiveFrom;
}