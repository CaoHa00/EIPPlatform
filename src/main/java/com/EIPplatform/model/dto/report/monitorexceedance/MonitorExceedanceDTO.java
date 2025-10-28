package com.EIPplatform.model.dto.report.monitorexceedance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorExceedanceDTO implements Serializable {
    private Integer exceedanceId;
    private UUID sectionId;
    private UUID reportId;
    private String pointName;
    private String pointSymbol;
    private LocalDate monitoringDate;
    private String longitude;
    private String latitude;
    private String exceededParam;
    private BigDecimal resultValue;
    private BigDecimal qcvnLimit;
    private String sectionType;
}