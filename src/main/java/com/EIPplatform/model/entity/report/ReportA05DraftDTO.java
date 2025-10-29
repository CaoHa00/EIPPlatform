package com.EIPplatform.model.entity.report;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportA05DraftDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    UUID reportId;
    UUID businessDetailId;
    Integer reportYear;
    String reportingPeriod;
    // Các phần dữ liệu (từng bước form)
    WasteWaterDataDTO wasteWaterData;
    // AirEmissionDataDTO airEmissionData;  // Bước tiếp theo
    // SolidWasteDataDTO solidWasteData;    // Bước tiếp theo


    // Metadata
    LocalDateTime lastModified;
    Integer currentStep;// Bước hiện tại user đang ở
    Boolean isDraft;// true = chưa submit, false = đã submit

    //Tracking 
    Integer completionPercentage;
}
