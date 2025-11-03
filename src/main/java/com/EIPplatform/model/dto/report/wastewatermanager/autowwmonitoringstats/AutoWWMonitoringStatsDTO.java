package com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringstats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoWWMonitoringStatsDTO {
    Long statId;
    String paramName;
    Integer valDesign;
    Integer valReceived;
    Integer valError;
    Double ratioReceivedDesign;
    Double ratioErrorReceived;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}