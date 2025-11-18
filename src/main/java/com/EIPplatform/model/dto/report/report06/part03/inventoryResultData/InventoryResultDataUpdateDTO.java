package com.EIPplatform.model.dto.report.report06.part03.inventoryResultData;
import com.EIPplatform.model.dto.report.report06.part03.emissionData.EmissionDataUpdateDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultUpdateDTO;
import com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation.UncertaintyEvaluationUpdateDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResultDataUpdateDTO {
    String methodCollectDataSources;
    String methodCollectDataSourcesOther;
    String calculationApproach;
    String calculationPrinciple;
    String sourceDescription;

    List<EmissionDataUpdateDTO> emissionDatas;

    ResultUpdateDTO resultEntity;

    List<UncertaintyEvaluationUpdateDTO> uncertaintyEvaluations;
}