package com.EIPplatform.model.dto.report.reportB04.part1.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
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
public class ThirdPartyImplementerCreationRequest {

    @NotBlank
    String orgName;

    @NotBlank
    String orgDocType;

    @NotBlank
    String orgDocNumber;

    @NotBlank
    String orgDocIssuer;

    @NotNull
    LocalDate orgDocIssueDate;

    LocalDate orgDocAmendDate;
}