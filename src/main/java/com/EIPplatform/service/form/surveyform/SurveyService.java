package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.mapper.form.surveyform.SurveyFormMapper;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.form.surveyform.*;
import com.EIPplatform.service.authentication.UserAccountImplementation;
import com.EIPplatform.service.form.submission.SubmissionService;
import com.EIPplatform.model.entity.form.surveyform.*;
import com.EIPplatform.model.dto.form.surveyform.survey.*;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurveyService implements SurveyServiceInterface {

    private final SurveyFormRepository surveyRepository;
    private final SurveyFormCategoryRepository surveyFormCategoryRepository;
    private final UserAccountImplementation userService;
    private final SurveySecurityService securityService;
    private final QuestionService questionService;
    private final SubmissionService submissionService;
    private final UserAccountRepository userAccountRepository;
    private final ExceptionFactory exceptionFactory;
    private final SurveyFormMapper surveyFormMapper;

    public SurveyService(SurveyFormRepository surveyRepository,
                         SurveyFormCategoryRepository surveyFormCategoryRepository, UserAccountImplementation userService, SurveySecurityService securityService,
                         QuestionService questionService, SubmissionService submissionService, UserAccountRepository userAccountRepository, ExceptionFactory exceptionFactory, SurveyFormMapper surveyFormMapper) {
        this.surveyRepository = surveyRepository;
        this.surveyFormCategoryRepository = surveyFormCategoryRepository;
        this.userService = userService;
        this.securityService = securityService;
        this.questionService = questionService;
        this.submissionService = submissionService;
        this.userAccountRepository = userAccountRepository;
        this.exceptionFactory = exceptionFactory;
        this.surveyFormMapper = surveyFormMapper;
    }

    @Transactional
    @Override
    public SurveyDTO createSurvey(CreateSurveyFormDTO dto) {
        //PLACEHOLDER USER, CREATE METHOD TO GET ACTUAL USER FROM REQUEST!!!!!!!!!
        UserAccount creator = userAccountRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No users exist in the database. Please create at least one user to serve as a placeholder."));

        SurveyForm form = new SurveyForm();
        form.setTitle(dto.getTitle());
        form.setDescription(dto.getDescription());
        form.setCreator(creator);
        form.setActive(true);

        if (dto.getCategoryId() != null) {
            SurveyFormCategory category = surveyFormCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyFormCategory", "id", dto.getCategoryId(), FormError.SURVEY_FORM_CATEGORY_NOT_FOUND));
            form.setSurveyFormCategory(category);
        }

        // DELEGATION
        if (dto.getQuestions() != null) {
            List<Question> questions = dto.getQuestions().stream()
                    .map(qDto -> questionService.buildQuestionEntity(qDto, form))
                    .collect(Collectors.toList());

            form.setQuestions(questions);
        }

        // SAVE ONCE. cascades all is enabled
        surveyRepository.save(form);

        return surveyFormMapper.toDTO(form);
    }

    @Transactional // edit TITLE and DESCRIPTION
    @Override
    public SurveyDTO editSurvey(UUID id, EditSurveyDTO dto) {
        SurveyForm form = securityService.getFormIfCreator(id);

        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            form.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            form.setDescription(dto.getDescription());
        }

        if (dto.getCategoryId() != null) {
            SurveyFormCategory category = surveyFormCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyFormCategory", "id", dto.getCategoryId(), FormError.SURVEY_FORM_CATEGORY_NOT_FOUND));
            form.setSurveyFormCategory(category);
        }

        form.setUpdatedAt(LocalDateTime.now());

        surveyRepository.save(form);
        return surveyFormMapper.toDTO(form);
    }

    @Transactional
    @Override
    public void activeSwitch(UUID id) {
        SurveyForm form = securityService.getFormIfCreator(id);

        boolean newActive = !form.isActive();
        form.setActive(newActive);
    }

    @Transactional
    @Override
    public SurveyDTO updateExpiry(UUID id, LocalDateTime expiresAt) {
        SurveyForm form = securityService.getFormIfCreator(id);

        form.setExpiresAt(expiresAt);

        form.setUpdatedAt(LocalDateTime.now());

        surveyRepository.save(form);
        return surveyFormMapper.toDTO(form);
    }

    /**
     * Hard delete SurveyForm from Database
     * Cannot be deleted if Submissions related to that SurveyForm entity still exists in the Database
     * @param id SurveyForm ID
     */
    @Transactional
    @Override
    public void hardDeleteSurvey(UUID id) {
        // Creator check
        SurveyForm form = securityService.getFormIfCreator(id);

        // Check for existing submission before deleting
        long submissionCount = submissionService.getSubmissionCountOfSurvey(id);
        if (submissionCount > 0) {
            throw exceptionFactory.createCustomException("SurveyForm",
                    Collections.singletonList("submissionCount"),
                    Collections.singletonList(submissionCount),
                    FormError.SUBMISSIONS_EXIST);
        }

        // DELEGATION: Clean up Questions => Questions will clean up Options
        questionService.batchDeleteQuestions(form);

        // same logic in QuestionService, clear the Questions list so Hibernate doesn't resave it
        if (form.getQuestions() != null) {
            form.getQuestions().clear();
        }

        // Delete the Form
        surveyRepository.deleteAllInBatch(List.of(form));
    }


    @Transactional(readOnly = true)
    @Override
    public SurveyDTO getSurveyDTO(UUID id) {
        SurveyForm form = surveyRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", id, FormError.SURVEY_FORM_NOT_FOUND));

        return surveyFormMapper.toDTO(form);
    }

    /**
     * Get all SurveyForms that belong to a creatorId
     * @param id user ID of the form creator
     * @return a SurveyDTO
     * @deprecated use searchSurveys() instead
     */
    @Deprecated
    @Override
    public List<SurveyDTO> getSurveyByCreatorId(UUID id) {
        //REPLACE this in the future maybe
        UserAccount creator = userAccountRepository.findById(id).orElseThrow(() -> exceptionFactory.createNotFoundException("User", "id", id, UserError.NOT_FOUND));

        List<SurveyForm> formList = surveyRepository.findByCreator(creator);
        return surveyFormMapper.toDTOList(formList);
    }

    @Override
    public List<SurveyDTO> getAllSurveys() {
        List<SurveyForm> formList = surveyRepository.findAll();
        return surveyFormMapper.toDTOList(formList);
    }

    /**
     * Get all SurveyForms that belong to a categoryId
     * @param categoryId category ID
     * @return a SurveyDTO
     * @deprecated use searchSurveys() instead
     */
    @Deprecated
    @Override
    public List<SurveyDTO> getAllSurveysByCategory(UUID categoryId){
        SurveyFormCategory category = surveyFormCategoryRepository.findById(categoryId)
                .orElseThrow(() -> exceptionFactory
                        .createNotFoundException("SurveyFormCategory", "id", categoryId, FormError.SURVEY_FORM_CATEGORY_NOT_FOUND));

        List<SurveyForm> formList = surveyRepository.findBySurveyFormCategory(category);

        return surveyFormMapper.toDTOList(formList);

    }

    /**
     *  Build search query
     * @param creatorId creatorId
     * @param categoryId categoryId
     * @param title title
     * @return SurveyDTO
     */
    @Override
    public List<SurveyDTO> searchSurveys(UUID creatorId, UUID categoryId, String title) {
        Specification<SurveyForm> spec = (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (creatorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("creator").get("id"), creatorId));
            }
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("surveyFormCategory").get("id"), categoryId));
            }
            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        List<SurveyForm> formList = surveyRepository.findAll(spec);
        return surveyFormMapper.toDTOList(formList);
    }
}
