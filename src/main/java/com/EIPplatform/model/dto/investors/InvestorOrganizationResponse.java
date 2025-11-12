package com.EIPplatform.model.dto.investors;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.EIPplatform.model.dto.legalDocs.LegalDocsResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorOrganizationResponse extends InvestorResponse {
    String organizationName;
    List<LegalDocsResponse> legalDocs;
}