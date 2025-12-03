package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.mapper.form.surveyform.SurveyFormMapper;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.form.surveyform.*;
import com.EIPplatform.service.authentication.UserAccountImplementation;
import com.EIPplatform.model.entity.form.surveyform.*;
import com.EIPplatform.model.dto.form.surveyform.survey.*;

import com.EIPplatform.service.form.submission.SubmissionServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService implements SurveyServiceInterface {

    private final SurveyFormRepository surveyRepository;
    private final SurveyAccessControlServiceInterface accessControlService;
    private final SubmissionServiceInterface submissionService;
    private final UserAccountRepository userAccountRepository;
    private final ExceptionFactory exceptionFactory;
    private final SurveyFormMapper surveyFormMapper;


    @Transactional
    @Override
    public SurveyDTO createSurvey(CreateSurveyFormDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        UserAccount creator = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("User", "id", userAccountId, UserError.NOT_FOUND));

        SurveyForm form = new SurveyForm();
        form.setTitle(dto.getTitle());
        form.setDescription(dto.getDescription());
        form.setCreator(creator);
        form.setActive(true);

        // Questions are no longer directly created on SurveyForm, they must be added to GroupDimensions later.
        
        // SAVE ONCE. cascades all is enabled
        surveyRepository.save(form);

        return surveyFormMapper.toDTO(form);
    }

    @Transactional // edit TITLE and DESCRIPTION
    @Override
    public SurveyDTO editSurvey(UUID id, EditSurveyDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        SurveyForm form = surveyRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", id, FormError.SURVEY_FORM_NOT_FOUND));

        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            form.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            form.setDescription(dto.getDescription());
        }

        form.setUpdatedAt(LocalDateTime.now());

        surveyRepository.save(form);
        return surveyFormMapper.toDTO(form);
    }

    @Transactional
    @Override
    public void activeSwitch(UUID id, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        SurveyForm form = surveyRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", id, FormError.SURVEY_FORM_NOT_FOUND));

        boolean newActive = !form.isActive();
        form.setActive(newActive);
    }

    @Transactional
    @Override
    public SurveyDTO updateExpiry(UUID id, LocalDateTime expiresAt, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        SurveyForm form = surveyRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", id, FormError.SURVEY_FORM_NOT_FOUND));

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
    public void hardDeleteSurvey(UUID id, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        SurveyForm form = surveyRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", id, FormError.SURVEY_FORM_NOT_FOUND));

        // Check for existing submission before deleting
        long submissionCount = submissionService.getSubmissionCountOfSurvey(id);
        if (submissionCount > 0) {
            throw exceptionFactory.createCustomException("SurveyForm",
                    Collections.singletonList("submissionCount"),
                    Collections.singletonList(submissionCount),
                    FormError.SUBMISSIONS_EXIST);
        }

        // everything should cascade from SurveyForm downwards

        surveyRepository.delete(form);
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
     *  Build search query
     * @param creatorId creatorId
     * @param title title
     * @return SurveyDTO
     */
    @Override
    public List<SurveyDTO> searchSurveys(UUID creatorId, String title) {
        Specification<SurveyForm> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (creatorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("creator").get("userAccountId"), creatorId));
            }
            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<SurveyForm> formList = surveyRepository.findAll(spec);
        return surveyFormMapper.toDTOList(formList);
    }

    @Override
    @Transactional
    public void updateSurveyUpdatedAt(UUID surveyFormId) {
        SurveyForm form = surveyRepository.findById(surveyFormId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", surveyFormId, FormError.SURVEY_FORM_NOT_FOUND));
        
        form.setUpdatedAt(LocalDateTime.now());
        surveyRepository.save(form);
    }
}