package com.EIPplatform.mapper.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.option.OptionDTO;
import com.EIPplatform.model.entity.form.surveyform.QuestionOption;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionOptionMapper {

    QuestionOptionMapper INSTANCE = Mappers.getMapper(QuestionOptionMapper.class);

    OptionDTO toDTO(QuestionOption option);

    List<OptionDTO> toDTOList(List<QuestionOption> options);
}
