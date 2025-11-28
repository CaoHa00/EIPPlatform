package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.mapper.form.surveyform.SurveyFormCategoryMapper;
import com.EIPplatform.model.dto.form.surveyform.survey.CreateSurveyFormCategoryDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.SurveyFormCategoryDTO;
import com.EIPplatform.model.entity.form.surveyform.QuestionCategory;
import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;
import com.EIPplatform.repository.form.surveyform.SurveyFormCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public SurveyFormCategoryDTO getById(UUID id){
        SurveyFormCategory sfc = surveyFormCategoryRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyFormCategory", "id", id, FormError.SURVEY_FORM_CATEGORY_NOT_FOUND));

        return categoryMapper.toDTO(sfc);
    }

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
