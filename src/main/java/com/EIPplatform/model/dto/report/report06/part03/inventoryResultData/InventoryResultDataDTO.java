package com.EIPplatform.model.dto.report.report06.part03.inventoryResultData;
import com.EIPplatform.model.dto.report.report06.part03.emissionData.EmissionDataDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultDTO;
import com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation.UncertaintyEvaluationDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResultDataDTO {
    UUID inventoryResultDataId;

    String methodCollectDataSources;
    String methodCollectDataSourcesOther;
    String calculationApproach;
    String calculationPrinciple;
    String sourceDescription;

    List<EmissionDataDTO> emissionDatas;

    String result; // auto-generated JSON, read-only

    ResultDTO resultEntity;

    List<UncertaintyEvaluationDTO> uncertaintyEvaluations;
}