package com.EIPplatform.model.dto.report.report05;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportA05DraftDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    UUID reportId;
    UUID businessDetailId;
    Integer reportYear;
    String reportingPeriod;

    // Các phần dữ liệu (từng bước form)
    WasteWaterDataDTO wasteWaterData;
    WasteManagementDataDTO wasteManagementData;
    AirEmissionDataDTO airEmissionData;
    // Metadata
    LocalDateTime lastModified;
    Integer currentStep; // Bước hiện tại user đang ở (e.g., 1 = WasteWater, 2 = WasteManagement, etc.)
    Boolean isDraft; // true = chưa submit, false = đã submit

    // Tracking
    Integer completionPercentage;
}