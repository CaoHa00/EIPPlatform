package com.EIPplatform.model.dto.form.surveyform.survey;

import lombok.Data;

import java.util.UUID;

@Data
public class EditSurveyDTO {
    //id is sent using api path variables
    private String title;

    private String description;

    private UUID categoryId;
}
