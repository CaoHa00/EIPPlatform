package com.EIPplatform.model.dto.report.report05;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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