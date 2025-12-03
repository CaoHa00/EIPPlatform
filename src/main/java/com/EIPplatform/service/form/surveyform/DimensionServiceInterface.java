package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.survey.CreateDimensionDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.DimensionDTO;

import java.util.List;
import java.util.UUID;

public interface DimensionServiceInterface {
    DimensionDTO getById(UUID id);
    List<DimensionDTO> getAll();
    List<DimensionDTO> getAllBySurveyFormId(UUID surveyFormId);
    List<DimensionDTO> createDimensionList(List<CreateDimensionDTO> list, UUID surveyFormId, UUID userAccountId);
    void deleteDimension(UUID id, UUID userAccountId);

    DimensionDTO editDimensionName(UUID id, String name, UUID userAccountId);
}
