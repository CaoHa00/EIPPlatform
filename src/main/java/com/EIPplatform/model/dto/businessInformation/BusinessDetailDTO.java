package com.EIPplatform.model.dto.businessInformation;
import com.EIPplatform.model.enums.OperationType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetailDTO {

    // String legalRepresentative;

    @NotBlank(message = "FIELD_REQUIRED")
    String phoneNumber;

    @NotBlank(message = "FIELD_REQUIRED")
    String facilityName;

    @NotBlank(message = "FIELD_REQUIRED")
    String address;

    @NotBlank(message = "FIELD_REQUIRED")
    String activityType;

    @NotBlank(message = "FIELD_REQUIRED")
    String scaleCapacity;
    String ISO_certificate_14001;

    @NotBlank(message = "FIELD_REQUIRED")
    String businessRegistrationNumber;

    @NotBlank(message = "FIELD_REQUIRED")
    String taxCode;

    @Email
    @Size(max = 255)
    String email;

    @NotNull(message = "Operation type is required")
    OperationType operationType = OperationType.REGULAR;

    @Size(max = 500, message = "Description too long")
    String seasonalDescription;

}
