package com.EIPplatform.model.dto.report.wastewatermanager.wastewatermonitoringexceedances;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteWaterMonitoringExceedancesDTO {
    Long exceedanceId;
    String pointName;
    String pointSymbol;
    String monitoringDate;
    String longitude;
    String latitude;
    String exceededParam;
    Double resultValue;
    Double qcvnLimit;
}