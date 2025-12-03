package com.EIPplatform.model.dto.form.surveyform.question;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeleteQuestionDTO {
    @NotEmpty(message = "The questionId list cannot be empty.")
    private List<UUID> questionIdsList;
}
