package com.EIPplatform.mapper.report.report06.part03;

import com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation.UncertaintyEvaluationCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation.UncertaintyEvaluationDTO;
import com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation.UncertaintyEvaluationUpdateDTO;
import com.EIPplatform.model.entity.report.report06.part03.UncertaintyEvaluation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UncertaintyEvaluationMapper {
    UncertaintyEvaluationMapper INSTANCE = Mappers.getMapper(UncertaintyEvaluationMapper.class);

    @Mapping(target = "uncertaintyEvaluationId", ignore = true)
    @Mapping(target = "inventoryResultData", ignore = true)
    @Mapping(target = "sourceCode", ignore = true)
    @Mapping(target = "sourceName", ignore = true)
    @Mapping(target = "combinedUncertainty", ignore = true)
    UncertaintyEvaluation toEntity(UncertaintyEvaluationCreateDTO dto);

    UncertaintyEvaluationDTO toDTO(UncertaintyEvaluation entity);

    @Mapping(target = "uncertaintyEvaluationId", ignore = true)
    @Mapping(target = "inventoryResultData", ignore = true)
    @Mapping(target = "sourceCode", ignore = true)
    @Mapping(target = "sourceName", ignore = true)
    @Mapping(target = "combinedUncertainty", ignore = true)
    void updateFromDTO(UncertaintyEvaluationUpdateDTO dto, @MappingTarget UncertaintyEvaluation entity);
}