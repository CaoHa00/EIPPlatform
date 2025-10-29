package com.EIPplatform.model.dto.userInformation;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {

    UUID userDetailId;

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

}
