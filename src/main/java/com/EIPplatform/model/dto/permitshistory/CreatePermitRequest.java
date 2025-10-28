package com.EIPplatform.model.dto.permitshistory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePermitRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 255, message = "MAX_LENGTH_EXCEEDED")
    String permitType;

    @Size(max = 100, message = "MAX_LENGTH_EXCEEDED")
    String permitNumber;

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 500, message = "MAX_LENGTH_EXCEEDED")
    String projectName;

    @NotNull(message = "FIELD_REQUIRED")
    @PastOrPresent(message = "INVALID_DATE_FUTURE")
    LocalDate issueDate;

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 500, message = "MAX_LENGTH_EXCEEDED")
    String issuerOrg;
}
