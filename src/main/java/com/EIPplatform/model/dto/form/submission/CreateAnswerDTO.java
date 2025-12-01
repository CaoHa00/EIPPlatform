package com.EIPplatform.model.dto.form.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAnswerDTO {
    @NotNull(message = "Question ID is required")
    private UUID questionId;

    // Frontend sends:
    // - "Hello World" (Text)
    // - "d290f1ee-6c54-4b01-90e6-d701748f0851" (Radio/Likert Option ID)
    // - "['id1', 'id2']" (Checkbox JSON String of Checkboxes Option ID)
    @NotBlank(message = "Answer value is required")
    private String value;
}
