package com.EIPplatform.model.dto.report.report;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionRemedyResponse {
    UUID reportId;
    String inspectionRemedyReport;
    LocalDateTime updatedAt;
}