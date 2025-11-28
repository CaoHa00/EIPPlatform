package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionCategoryDTO;
import com.EIPplatform.model.entity.form.surveyform.QuestionCategory;
import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;
import com.EIPplatform.repository.form.surveyform.QuestionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionCategoryService implements QuestionCategoryServiceInterface {

    private final QuestionCategoryRepository questionCategoryRepository;

    @Override
    public QuestionCategory buildQuestionCategoryEntity(CreateQuestionCategoryDTO dto, SurveyFormCategory parent) {
        QuestionCategory questionCategory = new QuestionCategory();
        questionCategory.setName(dto.getName());
        questionCategory.setSurveyFormCategory(parent);
        questionCategory.setCode(dto.getCode());
        return questionCategory;
    }
}
