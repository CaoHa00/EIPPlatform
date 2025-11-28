package com.EIPplatform.service.form.surveyform;


import com.EIPplatform.model.dto.form.surveyform.survey.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SurveyServiceInterface {
    SurveyDTO createSurvey(CreateSurveyFormDTO dto);

    SurveyDTO editSurvey(UUID id, EditSurveyDTO dto) throws IllegalAccessException;

    void activeSwitch(UUID id) throws IllegalAccessException;

    SurveyDTO updateExpiry(UUID id, LocalDateTime expiresAt) throws IllegalAccessException;

    void hardDeleteSurvey(UUID id) throws IllegalAccessException;

    SurveyDTO getSurveyDTO(UUID id);

    List<SurveyDTO> getSurveyByCreatorId(UUID id);

    List<SurveyDTO> getAllSurveys();
}