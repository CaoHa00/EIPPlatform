package com.EIPplatform.mapper.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.question.QuestionDTO;
import com.EIPplatform.model.entity.form.surveyform.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionOptionMapper.class)
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mapping(source = "questionCategory.categoryId", target = "categoryId")
    @Mapping(source = "questionCategory.name", target = "categoryName")
    @Mapping(source = "type", target = "type")
    QuestionDTO toDTO(Question question);

    List<QuestionDTO> toDTOList(List<Question> questions);
}
