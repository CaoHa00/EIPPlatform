package com.EIPplatform.model.dto.report.reportB04.part1.request;

import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationCreationRequest;
import com.EIPplatform.model.dto.businessInformation.project.ProjectCreateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class ReportInvestorDetailCreateRequest {
    // General detail from businessDetail
    @NotNull(message = "taxCode is required")
    String taxCode;
    @NotNull(message = "phoneNumber is required")
    String phoneNumber;

    String fax;
    @NotNull(message = "email is required")
    String email;

    InvestorIndividualCreationRequest investorIndividual;
    
    InvestorOrganizationCreationRequest investorOrganization;

    // Bên thứ 3 thực hiện
    @NotNull(message = "thirdPartyImplementer is required")
    ThirdPartyImplementerCreationRequest thirdPartyImplementer;

    // tên dự án
    @NotNull(message = "project is required")
    ProjectCreateRequest project;
}