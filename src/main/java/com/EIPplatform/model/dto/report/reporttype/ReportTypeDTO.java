package com.EIPplatform.model.dto.report.reporttype;
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
public class ReportTypeDTO implements Serializable {

    private Integer reportTypeId;
    private String reportName;
    private Integer reportTemplateId;
    private String templateName;
    private LocalDate dueDate;
    private String frequency;
    private String description;
}