package com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportedHwStatCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String wasteName;

    @NotBlank(message = "IS_REQUIRED")
    String hwCode;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double volumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String transporterOrg;

    @NotBlank(message = "IS_REQUIRED")
    String overseasProcessorOrg;

    @Size(max = 50)
    String baselCode;
}