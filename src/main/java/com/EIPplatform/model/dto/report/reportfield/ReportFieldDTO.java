package com.EIPplatform.model.dto.report.reportfield;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFieldDTO implements Serializable {
    private Integer fieldId;
    private UUID reportId;
    private String fieldName;
    private String fieldValue;
    private LocalDateTime createdAt;
}