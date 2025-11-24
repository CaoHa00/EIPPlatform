package com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecycleIndustrialWasteStatCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    String transferOrg;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double volumeCy;

    @NotBlank(message = "IS_REQUIRED")
    String unitCy;

    @NotBlank(message = "IS_REQUIRED")
    String wasteTypeDesc;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double volumePy;

    @NotBlank(message = "IS_REQUIRED")
    String unitPy;
}