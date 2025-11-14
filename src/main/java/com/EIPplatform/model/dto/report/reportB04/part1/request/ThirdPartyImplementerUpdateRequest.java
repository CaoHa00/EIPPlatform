package com.EIPplatform.model.dto.report.reportB04.part1.request;


import java.time.LocalDate;

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
public class ThirdPartyImplementerUpdateRequest {

    @NotNull
    Long tpiId;

    String orgName;
    String orgDocType;
    String orgDocNumber;
    String orgDocIssuer;
    LocalDate orgDocIssueDate;
    LocalDate orgDocAmendDate;
}