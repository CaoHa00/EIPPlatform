package com.EIPplatform.mapper.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.question.QuestionCategoryDTO;
import com.EIPplatform.model.entity.form.surveyform.QuestionCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionCategoryMapper {
    QuestionCategoryMapper INSTANCE = Mappers.getMapper(QuestionCategoryMapper.class);

    QuestionCategoryDTO toDTO(QuestionCategory questionCategory);

    List<QuestionCategoryDTO> toDTOList(List<QuestionCategory> questionCategories);

}
