package com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopInventoryStatCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    String popName;

    @DecimalMin(value = "0.0", inclusive = true)
    Double importVolume;

    String importUnit;

    @DecimalMin(value = "0.0", inclusive = true)
    Double volumeUsed;

    String usedUnit;

    @DecimalMin(value = "0.0", inclusive = true)
    Double volumeStocked;

    String stockedUnit;

    @Size(max = 50)
    String casCode;

    String importDate;

    @Size(max = 100)
    String concentration;

    @Size(max = 255)
    String complianceResult;
}