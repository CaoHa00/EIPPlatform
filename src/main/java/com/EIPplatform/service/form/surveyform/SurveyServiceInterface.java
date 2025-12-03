package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.survey.CreateSurveyFormDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.EditSurveyDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.SurveyDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SurveyServiceInterface {
    SurveyDTO createSurvey(CreateSurveyFormDTO dto, UUID userAccountId);
    SurveyDTO editSurvey(UUID id, EditSurveyDTO dto, UUID userAccountId);
    void activeSwitch(UUID id, UUID userAccountId);
    SurveyDTO updateExpiry(UUID id, LocalDateTime expiresAt, UUID userAccountId);
    void hardDeleteSurvey(UUID id, UUID userAccountId);
    SurveyDTO getSurveyDTO(UUID id);
    List<SurveyDTO> getSurveyByCreatorId(UUID id);
    List<SurveyDTO> getAllSurveys();
    List<SurveyDTO> searchSurveys(UUID creatorId, String title);
    void updateSurveyUpdatedAt(UUID surveyFormId);
}