package com.EIPplatform.model.dto.permitshistory;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePermitRequest {
    @Size(max = 255)
    String permitType;

    @Size(max = 100)
    String permitNumber;

    @Size(max = 500)
    String projectName;

    @PastOrPresent(message = "Issue date cannot be in the future")
    LocalDate issueDate;

    @Size(max = 500)
    String issuerOrg;
}