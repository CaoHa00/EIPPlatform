package com.EIPplatform.model.dto.form.surveyform.option;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeleteOptionDTO {
    @NotEmpty(message = "The OptionId list cannot be empty.")
    private List<UUID> optionIdsList;
}
