package com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarbonSinkProjectCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 255, message = "Project name must not exceed 255 characters")
    String projectName;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100, message = "Capture technology must not exceed 100 characters")
    String captureTechnology;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal capturedCo2AmountTons;

    @NotBlank(message = "IS_REQUIRED")
    String storageSiteLocation;

    @NotBlank(message = "IS_REQUIRED")
    String monitoringDetails;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal netCo2SequesteredTons;
}
