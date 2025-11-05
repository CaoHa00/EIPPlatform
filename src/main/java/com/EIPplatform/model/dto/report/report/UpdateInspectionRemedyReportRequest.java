package com.EIPplatform.model.dto.report.report;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInspectionRemedyReportRequest {
    @Size(max = 4000, message = "The inspection remedy report must not exceed 4000 characters")
    String inspectionRemedyReport;
}