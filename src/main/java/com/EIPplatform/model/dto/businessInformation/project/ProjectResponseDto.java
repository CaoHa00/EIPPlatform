package com.EIPplatform.model.dto.businessInformation.project;

import com.EIPplatform.model.dto.legalDoc.LegalDocResponse;
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
    UUID businessDetailId;
    List<LegalDocResponse> legalDocsList;
}
