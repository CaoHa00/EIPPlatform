package com.EIPplatform.model.dto.businessInformation.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {
      UUID projectId;
    String projectName;
    String projectLocation;
    String projectLegalDocType;
    String projectIssuerOrg;
    LocalDate projectIssueDate;
    LocalDate projectIssueDateLatest;
    UUID businessDetailId;
}
