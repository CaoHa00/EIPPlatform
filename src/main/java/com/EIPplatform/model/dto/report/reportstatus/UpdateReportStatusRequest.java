package com.EIPplatform.model.dto.report.reportstatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReportStatusRequest {
    private String statusName;
    private String statusCode;
    private Integer statusOrder;
    private String description;
}