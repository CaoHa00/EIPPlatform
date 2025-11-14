package com.EIPplatform.model.dto.investors;

import java.util.UUID;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.enums.InvestorType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "investorType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = InvestorOrganizationResponse.class, name = "ORGANIZATION"),
        @JsonSubTypes.Type(value = InvestorIndividualResponse.class, name = "INDIVIDUAL")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class InvestorResponse {
    UUID investorId;
    InvestorType investorType;
    String address;
    String taxCode;
    String phone;
    String fax;
    String email;
    AuditMetaData auditMetaData;
}