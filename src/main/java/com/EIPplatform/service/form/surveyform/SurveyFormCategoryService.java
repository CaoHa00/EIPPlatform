package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.mapper.form.surveyform.SurveyFormCategoryMapper;
import com.EIPplatform.model.dto.form.surveyform.survey.CreateSurveyFormCategoryDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.SurveyFormCategoryDTO;
import com.EIPplatform.model.entity.form.surveyform.QuestionCategory;
import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;
import com.EIPplatform.repository.form.surveyform.QuestionRepository;
import com.EIPplatform.repository.form.surveyform.SurveyFormCategoryRepository;
import com.EIPplatform.repository.form.surveyform.SurveyFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyFormCategoryService implements SurveyFormCategoryServiceInterface {

    private final SurveyFormCategoryRepository surveyFormCategoryRepository;
    private final QuestionCategoryService questionCategoryService;
    private final SurveyFormCategoryMapper categoryMapper;
    private final ExceptionFactory exceptionFactory;
    private final SurveyFormRepository surveyFormRepository;
    private final QuestionRepository questionRepository;


    @Override
    public SurveyFormCategoryDTO getById(UUID id){
        SurveyFormCategory sfc = surveyFormCategoryRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyFormCategory", "id", id, FormError.SURVEY_FORM_CATEGORY_NOT_FOUND));

        return categoryMapper.toDTO(sfc);
    }

    @Override
    public List<SurveyFormCategoryDTO> getAll(){
        return categoryMapper.toDTOList(surveyFormCategoryRepository.findAll());
    }

    @Override
    @Transactional
    public List<SurveyFormCategoryDTO> createCategoryList(List<CreateSurveyFormCategoryDTO> createSurveyFormCategoryDTOs) {
        List<SurveyFormCategory> surveyFormCategories = createSurveyFormCategoryDTOs.stream()
                .map(this::buildSurveyFormCategoryEntity)
                .collect(Collectors.toList());

        List<SurveyFormCategory> savedCategories = surveyFormCategoryRepository.saveAll(surveyFormCategories);

        return categoryMapper.toDTOList(savedCategories);
    }

    /**
     * Delete a SurveyFormCategory (SFC)
     * Pre-checks if there are any SurveyForm or Question using this SFC
     * @param categoryId The ID of the SurveyFormCategory
     */
    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        if (!surveyFormCategoryRepository.existsById(categoryId)) {
            throw exceptionFactory.createNotFoundException("SurveyFormCategory", "id", categoryId, FormError.SURVEY_FORM_CATEGORY_NOT_FOUND);
        }

        //check if any SurveyForm using this Category exists
        if (surveyFormRepository.existsBySurveyFormCategory_CategoryId(categoryId)) {
            throw exceptionFactory.createCustomException("SurveyFormCategory",
                    Collections.singletonList("id"),
                    Collections.singletonList(categoryId),
                    FormError.SURVEY_FORM_CATEGORY_IN_USE);
        }

        //check if any Question using the QuestionCategory in this SurveyForm exists
        if (questionRepository.existsByQuestionCategory_SurveyFormCategory_CategoryId(categoryId)) {
            throw exceptionFactory.createCustomException("SurveyFormCategory",
                    Collections.singletonList("id"),
                    Collections.singletonList(categoryId),
                    FormError.SURVEY_FORM_CATEGORY_IN_USE);
        }

        surveyFormCategoryRepository.deleteById(categoryId);
    }

    private SurveyFormCategory buildSurveyFormCategoryEntity(CreateSurveyFormCategoryDTO dto) {
        SurveyFormCategory surveyFormCategory = new SurveyFormCategory();
        surveyFormCategory.setName(dto.getName());

        if (dto.getQuestionCategories() != null && !dto.getQuestionCategories().isEmpty()) {
            List<QuestionCategory> questionCategories = dto.getQuestionCategories().stream()
                    .map(qDto -> questionCategoryService.buildQuestionCategoryEntity(qDto, surveyFormCategory))
                    .toList();
            surveyFormCategory.setQuestionCategories(new HashSet<>(questionCategories));
        }
        return surveyFormCategory;
    }
}
