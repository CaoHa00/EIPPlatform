package com.EIPplatform.mapper.form.submission;

import com.EIPplatform.model.dto.form.submission.AnswerDTO;
import com.EIPplatform.model.dto.form.submission.SubmissionDTO;
import com.EIPplatform.model.entity.form.submission.Answer;
import com.EIPplatform.model.entity.form.submission.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AnswerMapper.class})
public interface SubmissionMapper {
    SubmissionMapper INSTANCE = Mappers.getMapper(SubmissionMapper.class);

    @Mapping(source = "createdAt", target = "submittedAt")
    @Mapping(source = "surveyForm.id", target = "surveyFormId")
    @Mapping(source = "surveyForm.title", target = "surveyFormTitle")
    @Mapping(source = "surveyForm.surveyFormCategory.name", target = "surveyFormCategory")
    @Mapping(source = "respondent.userAccountId", target = "respondentId")
    @Mapping(source = "respondent.fullName", target = "respondentUsername")
    SubmissionDTO toDTO(Submission submission);

    List<SubmissionDTO> toDTOList(List<Submission> submissions);

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.text", target = "questionText")
    AnswerDTO toAnswerDTO(Answer answer);
}
