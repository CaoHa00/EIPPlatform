package com.EIPplatform.model.dto.report.report;

import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportA05DTO {
    UUID reportId;
    String reportCode;
    UUID businessDetailId;
    String facilityName;
    Integer reportYear;
    String reportingPeriod;
    String reviewNotes;
    String inspectionRemedyReport;
    BigDecimal completionPercentage;
    LocalDateTime createdAt;

    WasteWaterDataDTO wasteWaterData;
    WasteManagementDataDTO wasteManagementData;
    AirEmissionDataDTO airEmissionData;
}