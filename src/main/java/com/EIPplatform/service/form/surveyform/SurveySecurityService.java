package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.model.entity.form.surveyform.*;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.form.surveyform.*;
import com.EIPplatform.service.authentication.UserAccountImplementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Deprecated
public class SurveySecurityService implements SurveySecurityServiceInterface {

    private final SurveyFormRepository surveyRepository;
    private final QuestionOptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final ExceptionFactory exceptionFactory;

    /**
     * Checks if current user is creator.
     * Returns the Form if allowed.
     * Throws exception if not found or not authorized.
     */
    public SurveyForm getFormIfCreator(UUID formId, UUID userAccountId) {
        SurveyForm form = surveyRepository.findById(formId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", formId, FormError.SURVEY_FORM_NOT_FOUND));

        if (!form.getCreator().getUserAccountId().equals(userAccountId)) {
            throw exceptionFactory.createCustomException(ForbiddenError.FORBIDDEN);
        }

        return form;
    }

    /**
     * Checks if current user is creator of the form that owns this question.
     * Returns the Question if allowed.
     */
    public Question getQuestionIfCreator(UUID questionId, UUID userAccountId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", questionId, FormError.QUESTION_NOT_FOUND));

        if (!question.getGroupDimension().getDimension().getSurveyForm().getCreator().getUserAccountId().equals(userAccountId)) {
            throw exceptionFactory.createCustomException(ForbiddenError.FORBIDDEN);
        }

        return question;
    }

    /**
     * Checks if current user is creator of the form that owns this question option.
     * Returns the QuestionOption if allowed.
     */
    public QuestionOption getOptionIfCreator(UUID optionId, UUID userAccountId) {
        QuestionOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionOption", "id", optionId, FormError.QUESTION_NOT_FOUND)); // Assuming you'll add a QUESTION_OPTION_NOT_FOUND

        // Option -> Question -> GroupDimension -> Dimension -> Form -> Creator
        if (!option.getQuestion().getGroupDimension().getDimension().getSurveyForm().getCreator().getUserAccountId().equals(userAccountId)) {
            throw exceptionFactory.createCustomException(ForbiddenError.FORBIDDEN);
        }

        return option;
    }
}
