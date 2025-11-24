package com.EIPplatform.model.dto.businessInformation.project;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
