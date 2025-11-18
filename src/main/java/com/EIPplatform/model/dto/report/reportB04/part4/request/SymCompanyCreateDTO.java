package com.EIPplatform.model.dto.report.reportB04.part4.request;

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
public class SymCompanyCreateDTO {
    Long scId;
    String symCompanyName;
    String symCompanyType;
    @NotNull
    Long symbiosisIndustryId; // Reference to parent entity
}