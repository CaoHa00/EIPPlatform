package com.EIPplatform.model.dto.form.surveyform.survey;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpiryRequest {
    private LocalDateTime expiresAt;
}
