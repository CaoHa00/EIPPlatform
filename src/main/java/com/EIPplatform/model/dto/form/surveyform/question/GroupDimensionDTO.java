package com.EIPplatform.model.dto.form.surveyform.question;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class GroupDimensionDTO {
    private UUID id;
    private String name;
    private String code;
    private Set<QuestionDTO> questions;

}