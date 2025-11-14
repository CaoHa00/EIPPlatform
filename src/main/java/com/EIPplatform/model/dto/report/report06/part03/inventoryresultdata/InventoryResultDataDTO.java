package com.EIPplatform.model.dto.report.report06.part03.inventoryresultdata;

import com.EIPplatform.model.dto.report.report06.part03.emissiondata.EmissionDataDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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

    String result; // Auto-generated JSON

    @Valid
    List<@Valid EmissionDataDTO> emissionDatas;

    ResultDTO resultEntity;

    LocalDateTime createdAt;
}