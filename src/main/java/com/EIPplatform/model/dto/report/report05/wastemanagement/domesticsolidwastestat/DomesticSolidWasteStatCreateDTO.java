package com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat;

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
public class DomesticSolidWasteStatCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String wasteTypeName;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double volumeCy;

    @NotBlank(message = "IS_REQUIRED")
    String receiverOrg;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double volumePy;
}