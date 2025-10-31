package com.EIPplatform.model.dto.report.report.wastewatermanagerDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoWWMonitoringStatsDTO implements Serializable {
    Long statId;
    String paramName;
    Integer valDesign;
    Integer valReceived;
    Integer valError;
    BigDecimal ratioReceivedDesign;
    BigDecimal ratioErrorReceived;
}