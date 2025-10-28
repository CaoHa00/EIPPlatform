package com.EIPplatform.model.dto.report.reportstatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatusDTO implements Serializable {

    private Integer statusId;
    private String statusName;
    private String statusCode;
    private Integer statusOrder;
    private String description;
    private String statusColor;
}