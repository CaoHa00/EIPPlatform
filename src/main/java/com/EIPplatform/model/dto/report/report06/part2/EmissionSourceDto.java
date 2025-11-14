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
public class EmissionSourceDto {

    private  UUID id;

    private UUID operationalActivityDataId;

    @NotBlank(message = "FIELD_REQUIRED")
    private String sourceScope;

    @NotBlank(message = "FIELD_REQUIRED")
    private String sourceCode;

    @NotBlank(message = "FIELD_REQUIRED")
    private String sourceCategory;

    @NotBlank(message = "FIELD_REQUIRED")
    private String sourceName;

    private String sourceDescription;

    @NotBlank(message = "FIELD_REQUIRED")
    private BigDecimal ghgEmitted;

    @NotBlank(message = "FIELD_REQUIRED")
    private String dataInputSource;

    @NotBlank(message = "FIELD_REQUIRED")
    private String sourceUnit;

}
