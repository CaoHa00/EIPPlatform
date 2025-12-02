package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.EditQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionDTO;
import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.model.entity.form.surveyform.SurveyForm;

import java.util.UUID;

public interface QuestionServiceInterface {
    QuestionDTO getQuestion(UUID id);
    void reorderQuestions(UUID surveyId, ReorderRequestDTO dto);
    QuestionDTO editQuestion(UUID questionId, EditQuestionDTO dto);
    Question buildQuestionEntity(CreateQuestionDTO dto, SurveyForm parent);
    QuestionDTO addQuestion(CreateQuestionDTO dto, UUID parentId);
    void activeSwitch(UUID id);
    void hardDeleteQuestion(UUID questionId);
    void batchDeleteQuestions(SurveyForm form);
}
