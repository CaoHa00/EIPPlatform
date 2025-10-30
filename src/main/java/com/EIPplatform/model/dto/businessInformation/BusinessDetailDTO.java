package com.EIPplatform.model.dto.businessInformation;

import com.EIPplatform.model.enums.OperationType;
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
    @NotBlank(message = "FIELD_REQUIRED")
    String companyName;

    @NotBlank(message = "FIELD_REQUIRED")
    String legalPresentative;

    @NotBlank(message = "FIELD_REQUIRED")
    String phoneNumber;

    @NotBlank(message = "FIELD_REQUIRED")
    String location;

    @NotBlank(message = "FIELD_REQUIRED")
    String industrySector;

    @NotBlank(message = "FIELD_REQUIRED")
    String scaleCapacity;
    String ISO_certificate_14001;

    @NotBlank(message = "FIELD_REQUIRED")
    String businessRegistrationNumber;

    @NotBlank(message = "FIELD_REQUIRED")
    String taxCode;

    @NotNull(message = "Operation type is required")
    OperationType operationType = OperationType.REGULAR;

    @Size(max = 500, message = "Description too long")
    String seasonalDescription;
}
