package com.EIPplatform.model.dto.form.submission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class EditAnswerRequestDTO {
    @NotNull(message = "Answer ID is required")
    private UUID answerId;
    private String value;
}
