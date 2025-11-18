package com.EIPplatform.model.dto.report.report06;

import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataDTO;
import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report06DraftDTO {
    UUID report06Id;
    UUID businessDetailId;
    Integer reportYear;
    String reportingPeriod;

    InventoryResultDataDTO inventoryResultData;
    OperationalActivityDataDTO operationalActivityData;

    LocalDateTime lastModified;
    Integer currentStep;
    Boolean isDraft;
}
