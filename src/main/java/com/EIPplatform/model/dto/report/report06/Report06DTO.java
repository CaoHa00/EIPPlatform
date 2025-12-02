package com.EIPplatform.model.dto.report.report06;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataDTO;
import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report06DTO {
    UUID report06Id;
    String reportCode;
    UUID businessDetailId;
    String facilityName;
    Integer reportYear;
    String reportingPeriod;
    String reviewNotes;
    String inspectionRemedyReport;
    LocalDateTime createdAt;
    Boolean isDeleted;

    OperationalActivityDataDTO operationalActivityDataDTO;
    InventoryResultDataDTO inventoryResultDataDTO;
}
