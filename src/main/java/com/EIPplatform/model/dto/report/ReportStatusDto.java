package com.EIPplatform.model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatusDto {
    private Long reportStatusId;
    private String reportStatusName;
}
