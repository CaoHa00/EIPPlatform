package com.EIPplatform.model.dto.report.report06.part02.emissionSource;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmissionSourceCreateDTO {
    @NotNull(message = "IS_REQUIRED")
    @Min(value = 1, message = "Scope must be between 1-3")
    @Max(value = 3, message = "Scope must be between 1-3")
    Integer sourceScope;

    @NotBlank(message = "IS_REQUIRED")
    String sourceCategory;

    @NotBlank(message = "IS_REQUIRED")
    String sourceName;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100, message = "Source code must not exceed 100 characters")
    String sourceCode;

    String sourceDescription;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 255, message = "Data input source must not exceed 255 characters")
    String dataInputSource;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100, message = "Source unit must not exceed 100 characters")
    String sourceUnit;
}