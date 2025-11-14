package com.EIPplatform.model.dto.report.report06.part2;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarbonSinkProjectDto {

    private UUID carbonSinkProjectId;

    private UUID operationalActivityData;

    @NotBlank(message = "FIELD_REQUIRED")
    private String projectName;

    @NotBlank(message = "FIELD_REQUIRED")
    private String captureTechnology;

    @NotBlank(message = "FIELD_REQUIRED")
    private BigDecimal capturedCo2AmountTons;

    @NotBlank(message = "FIELD_REQUIRED")
    private String storageSiteLocation;

    @NotBlank(message = "FIELD_REQUIRED")
    private String monitoringDetails;

    @NotBlank(message = "FIELD_REQUIRED")
    private BigDecimal netCo2SequesteredTons;

}
