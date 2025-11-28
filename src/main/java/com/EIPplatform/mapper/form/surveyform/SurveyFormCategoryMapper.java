package com.EIPplatform.mapper.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.survey.SurveyFormCategoryDTO;
import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionCategoryMapper.class)
public interface SurveyFormCategoryMapper {
    SurveyFormCategoryMapper INSTANCE = Mappers.getMapper(SurveyFormCategoryMapper.class);

    SurveyFormCategoryDTO toDTO(SurveyFormCategory surveyFormCategory);

    List<SurveyFormCategoryDTO> toDTOList(List<SurveyFormCategory> surveyFormCategories);
}
