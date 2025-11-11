package com.EIPplatform.model.dto.report.report06.part03.inventoryresultdata;

import com.EIPplatform.model.dto.report.report06.part03.emissiondata.EmissionDataUpdateDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultUpdateDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
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

    @Valid
    List<@Valid EmissionDataUpdateDTO> emissionDatas;

    ResultUpdateDTO resultEntity;
}