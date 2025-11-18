package com.EIPplatform.model.dto.report.report06.part03.inventoryResultData;

import com.EIPplatform.model.dto.report.report06.part03.emissionData.EmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation.UncertaintyEvaluationCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResultDataCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String methodCollectDataSources;

    @NotBlank(message = "IS_REQUIRED")
    String methodCollectDataSourcesOther;

    @NotBlank(message = "IS_REQUIRED")
    String calculationApproach;

    @NotBlank(message = "IS_REQUIRED")
    String calculationPrinciple;

    @NotBlank(message = "IS_REQUIRED")
    String sourceDescription;

    @Valid
    @NotNull(message = "IS_REQUIRED")
    List<EmissionDataCreateDTO> emissionDatas;

    @NotNull(message = "IS_REQUIRED")
    String result;

    @Valid
    @NotNull(message = "IS_REQUIRED")
    ResultCreateDTO resultEntity;

    @Valid
    @NotNull(message = "IS_REQUIRED")
    List<UncertaintyEvaluationCreateDTO> uncertaintyEvaluations;
}