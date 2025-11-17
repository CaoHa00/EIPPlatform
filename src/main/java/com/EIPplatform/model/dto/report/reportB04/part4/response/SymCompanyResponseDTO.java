package com.EIPplatform.model.dto.report.reportB04.part4.response;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SymCompanyResponseDTO {
    Long scId;
    String symCompanyName;
    String symCompanyType;
    Long symbiosisIndustryId; // Reference to parent entity
}