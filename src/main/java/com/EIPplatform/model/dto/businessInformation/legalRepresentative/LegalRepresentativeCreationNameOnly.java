package com.EIPplatform.model.dto.businessInformation.legalRepresentative;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalRepresentativeCreationNameOnly {

    @NotBlank(message = "FIELD_REQUIRED")
    String name;

}