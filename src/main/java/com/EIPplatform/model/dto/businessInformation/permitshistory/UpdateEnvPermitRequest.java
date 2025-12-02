package com.EIPplatform.model.dto.businessInformation.permitshistory;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEnvPermitRequest {
    private String permitNumber;
    private LocalDate issueDate;
    private String issuerOrg;
    private String projectName;
}