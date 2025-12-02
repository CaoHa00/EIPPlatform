package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.mapper.form.surveyform.QuestionCategoryMapper;
import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionCategoryDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionCategoryDTO;
import com.EIPplatform.model.entity.form.surveyform.QuestionCategory;
import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;
import com.EIPplatform.repository.form.surveyform.QuestionCategoryRepository;
import com.EIPplatform.repository.form.surveyform.QuestionRepository;
import com.EIPplatform.repository.form.surveyform.SurveyFormCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionCategoryService implements QuestionCategoryServiceInterface {

    private final QuestionCategoryRepository questionCategoryRepository;
    private final SurveyFormCategoryRepository surveyFormCategoryRepository;
    private final QuestionCategoryMapper categoryMapper;
    private final ExceptionFactory exceptionFactory;
    private final QuestionRepository questionRepository;

    @Override
    public QuestionCategoryDTO getById(UUID id) {
        QuestionCategory qc = questionCategoryRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionCategory", "id", id, FormError.QUESTION_CATEGORY_NOT_FOUND));
        return categoryMapper.toDTO(qc);
    }

    @Override
    public List<QuestionCategoryDTO> getAll() {
        return categoryMapper.toDTOList(questionCategoryRepository.findAll());
    }

    @Override
    @Transactional
    public List<QuestionCategoryDTO> createCategoryList(List<CreateQuestionCategoryDTO> createQuestionCategoryDTOs, UUID surveyFormCategoryId) {
        SurveyFormCategory parentCategory = surveyFormCategoryRepository.findById(surveyFormCategoryId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyFormCategory", "id", surveyFormCategoryId, FormError.SURVEY_FORM_CATEGORY_NOT_FOUND));

        List<QuestionCategory> questionCategories = createQuestionCategoryDTOs.stream()
                .map(dto -> buildQuestionCategoryEntity(dto, parentCategory))
                .collect(Collectors.toList());

        List<QuestionCategory> savedCategories = questionCategoryRepository.saveAll(questionCategories);

        return categoryMapper.toDTOList(savedCategories);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        if (!questionCategoryRepository.existsById(categoryId)) {
            throw exceptionFactory.createNotFoundException("QuestionCategory", "id", categoryId, FormError.QUESTION_CATEGORY_NOT_FOUND);
        }

        //check if any existing Question entity uses this QuestionCategory
        if (questionRepository.existsByQuestionCategory_CategoryId(categoryId)) {
            throw exceptionFactory.createCustomException("QuestionCategory",
                    Collections.singletonList("id"),
                    Collections.singletonList(categoryId),
                    FormError.QUESTION_CATEGORY_IN_USE);
        }

        questionCategoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional
    public QuestionCategoryDTO editCategory(UUID categoryId, CreateQuestionCategoryDTO dto) {
        QuestionCategory category = questionCategoryRepository.findById(categoryId).orElseThrow(() ->
                exceptionFactory.createNotFoundException("QuestionCategory", "id", categoryId, FormError.QUESTION_CATEGORY_NOT_FOUND));

        category.setName(dto.getName());
        category.setCode(dto.getCode());

        questionCategoryRepository.save(category);

        return categoryMapper.toDTO(category);
    }

    public QuestionCategory buildQuestionCategoryEntity(CreateQuestionCategoryDTO dto, SurveyFormCategory parent) {
        QuestionCategory questionCategory = new QuestionCategory();
        questionCategory.setName(dto.getName());
        questionCategory.setSurveyFormCategory(parent);
        questionCategory.setCode(dto.getCode());
        return questionCategory;
    }
}
