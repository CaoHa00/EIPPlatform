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
public class LimitationDto {

    private  UUID id;

    private UUID operationalActivityDataId;

    @NotBlank(message = "FIELD_REQUIRED")
    private String limitationName;

    @NotBlank(message = "FIELD_REQUIRED")
    private String limitationDescription;

    private String improvementPlan;
}
