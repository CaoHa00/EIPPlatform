package com.EIPplatform.model.dto.report.report.wastewatermanagerDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteWaterMonitoringExceedancesDTO {
     Long exceedanceId;
    String pointName;
    String pointSymbol;
    LocalDate monitoringDate;
    String longitude;
    String latitude;
    String exceededParam;
    BigDecimal resultValue;
    BigDecimal qcvnLimit;
}
