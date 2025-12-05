package com.EIPplatform.mapper.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.question.GroupDimensionDTO;
import com.EIPplatform.model.entity.form.surveyform.GroupDimension;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupDimensionMapper {
    GroupDimensionMapper INSTANCE = Mappers.getMapper(GroupDimensionMapper.class);

    GroupDimensionDTO toDTO(GroupDimension groupDimension);

    List<GroupDimensionDTO> toDTOList(List<GroupDimension> groupDimensions);

}