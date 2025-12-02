package com.EIPplatform.model.dto.report.reportB04.part1;

import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorResponse;
import com.EIPplatform.model.dto.businessInformation.project.ProjectDTO;
import com.EIPplatform.model.dto.legalDoc.LegalDocDTO;

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
public class ReportInvestorDetailDTO {
    Long ridId;

    // General detail from businessDetail
    String taxCode;
    String phoneNumber;
    String fax;
    String email;

    // Investor from businessDetail
    InvestorResponse investor;

    InvestorIndividualResponse investorIndividual;

    InvestorOrganizationResponse investorOrganization;

    // Bên thứ 3 thực hiện
    ThirdPartyImplementerDTO thirdPartyImplementer;

    // tên dự án
    ProjectDTO project;
}
