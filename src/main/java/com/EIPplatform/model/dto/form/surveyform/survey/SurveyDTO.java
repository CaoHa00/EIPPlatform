package com.EIPplatform.model.dto.form.surveyform.survey;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class SurveyDTO {
    private UUID id;
    private String title;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private UUID creatorId;
    private String creatorName;
    private List<DimensionDTO> dimensions;
}
