package com.EIPplatform.model.dto.form.surveyform.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateGroupDimensionDTO {
    @NotBlank(message = "Group dimension name is required")
    private String name;

    @NotBlank(message = "Group dimension code is required. E.G: Indicator Group 1")
    private String code;
}