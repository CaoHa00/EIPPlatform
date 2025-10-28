package com.EIPplatform.model.dto.permitshistory;

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
public class UpdatePermitRequest implements Serializable {

    @Size(max = 255, message = "MAX_LENGTH_EXCEEDED")
    String permitType;

    @Size(max = 100, message = "MAX_LENGTH_EXCEEDED")
    String permitNumber;

    @Size(max = 500, message = "MAX_LENGTH_EXCEEDED")
    String projectName;

    @PastOrPresent(message = "INVALID_DATE_FUTURE")
    LocalDate issueDate;

    @Size(max = 500, message = "MAX_LENGTH_EXCEEDED")
    String issuerOrg;
}
