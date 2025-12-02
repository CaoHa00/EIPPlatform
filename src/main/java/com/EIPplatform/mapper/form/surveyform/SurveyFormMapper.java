package com.EIPplatform.mapper.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.survey.SurveyDTO;
import com.EIPplatform.model.entity.form.surveyform.SurveyForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface SurveyFormMapper {

    SurveyFormMapper INSTANCE = Mappers.getMapper(SurveyFormMapper.class);

    @Mapping(source = "creator.userAccountId", target = "creatorId")
    @Mapping(source = "creator.fullName", target = "creatorName")
    @Mapping(source = "surveyFormCategory.categoryId", target = "categoryId")
    @Mapping(source = "surveyFormCategory.name", target = "categoryName")
    SurveyDTO toDTO(SurveyForm surveyForm);

    @Mapping(source = "creator.userAccountId", target = "creatorId")
    @Mapping(source = "creator.fullName", target = "creatorName")
    @Mapping(source = "surveyFormCategory.categoryId", target = "categoryId")
    @Mapping(source = "surveyFormCategory.name", target = "categoryName")
    List<SurveyDTO> toDTOList(List<SurveyForm> surveyFormList);
}
