package com.EIPplatform.mapper.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.survey.DimensionDTO;
import com.EIPplatform.model.entity.form.surveyform.Dimension;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = GroupDimensionMapper.class)
public interface DimensionMapper {
    DimensionMapper INSTANCE = Mappers.getMapper(DimensionMapper.class);

    DimensionDTO toDTO(Dimension dimension);

    List<DimensionDTO> toDTOList(List<Dimension> dimensions);
}
