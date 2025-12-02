package com.EIPplatform.mapper.form.submission;

import com.EIPplatform.model.dto.form.submission.AnswerDTO;
import com.EIPplatform.model.entity.form.submission.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.text", target = "questionText")
    @Mapping(source = "question.questionCategory.name", target = "questionCategory")
    AnswerDTO toDTO(Answer answer);

    List<AnswerDTO> toDTOList(List<Answer> answers);
}
