package com.EIPplatform.model.dto.businessInformation.project;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    UUID projectId;
    String projectName;
    String projectLocation;
    String projectLegalDocType;
    String projectIssuerOrg;
    LocalDate projectIssueDate;
    LocalDate projectIssueDateLatest;
    UUID businessDetailId;
}
