package com.EIPplatform.model.dto.businessInformation.project;

import com.EIPplatform.model.dto.legalDocs.LegalDocsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {
    UUID projectId;
    String projectName;
    String projectLocation;
    UUID businessId;
    List<LegalDocsResponse> legalDocsList;
}
