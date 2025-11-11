package com.EIPplatform.model.dto.report.report06.part03.emissiondata;

import com.EIPplatform.model.dto.report.report06.part03.monthlyemissiondata.MonthlyEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.uncertaintyevaluation.UncertaintyEvaluationCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmissionDataCreateDTO {

    @NotNull(message = "IS_REQUIRED")
    UUID emissionSourceId;

    @Valid
    List<@Valid MonthlyEmissionDataCreateDTO> monthlyDatas;

    @NotNull(message = "IS_REQUIRED")
    @Valid
    UncertaintyEvaluationCreateDTO uncertaintyEvaluation;
}