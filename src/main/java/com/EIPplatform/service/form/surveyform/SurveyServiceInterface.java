package com.EIPplatform.service.form.surveyform;


import com.EIPplatform.model.dto.form.surveyform.survey.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SurveyServiceInterface {
    SurveyDTO createSurvey(CreateSurveyFormDTO dto);

    SurveyDTO editSurvey(UUID id, EditSurveyDTO dto);

    void activeSwitch(UUID id);

    SurveyDTO updateExpiry(UUID id, LocalDateTime expiresAt);

    void hardDeleteSurvey(UUID id);

    SurveyDTO getSurveyDTO(UUID id);

    List<SurveyDTO> getSurveyByCreatorId(UUID id);

    List<SurveyDTO> getAllSurveys();
    List<SurveyDTO> getAllSurveysByCategory(UUID categoryId);
    List<SurveyDTO> searchSurveys(UUID creatorId, UUID categoryId, String title);
}