package com.EIPplatform.model.dto.report.report06.part03.inventoryresultdata;

import com.EIPplatform.model.dto.report.report06.part03.emissiondata.EmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    String methodCollectDataSourcesOther;

    @NotBlank(message = "IS_REQUIRED")
    String calculationApproach;

    String calculationPrinciple;

    String sourceDescription;

    @Valid
    List<@Valid EmissionDataCreateDTO> emissionDatas;

    @Valid
    ResultCreateDTO resultEntity;
}