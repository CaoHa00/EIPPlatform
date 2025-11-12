package com.EIPplatform.model.dto.report.report05;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;
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
    Double completionPercentage;
    LocalDateTime createdAt;

    WasteWaterDataDTO wasteWaterData;
    WasteManagementDataDTO wasteManagementData;
    AirEmissionDataDTO airEmissionData;
}