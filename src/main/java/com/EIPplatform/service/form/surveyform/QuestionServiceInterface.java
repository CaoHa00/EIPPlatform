package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.DeleteQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.EditQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionDTO;
import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.model.entity.form.surveyform.SurveyForm;

import java.util.List;
import java.util.UUID;

public interface QuestionServiceInterface {
    QuestionDTO getQuestion(UUID id);
    void reorderQuestions(UUID groupDimensionId, ReorderRequestDTO dto, UUID userAccountId);
    QuestionDTO editQuestion(UUID questionId, EditQuestionDTO dto, UUID userAccountId);
    Question buildQuestionEntity(CreateQuestionDTO dto);
    QuestionDTO addQuestion(CreateQuestionDTO dto, UUID userAccountId);
    void activeSwitch(UUID id, UUID userAccountId);
    void hardDeleteQuestion(UUID questionId, UUID userAccountId);
    void batchDeleteQuestions(DeleteQuestionDTO dto, UUID userAccountId);
}