package com.EIPplatform.model.dto.report.report05;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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