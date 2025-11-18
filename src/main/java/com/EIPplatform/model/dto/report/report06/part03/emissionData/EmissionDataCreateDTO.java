package com.EIPplatform.model.dto.report.report06.part03.emissionData;

import com.EIPplatform.model.dto.report.report06.part03.monthlyEmissionData.MonthlyEmissionDataCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmissionDataCreateDTO {
    @NotNull(message = "IS_REQUIRED")
    List<@Valid MonthlyEmissionDataCreateDTO> monthlyDatas;

    @NotBlank(message = "IS_REQUIRED")
    BigDecimal totalAnnualData;
}
