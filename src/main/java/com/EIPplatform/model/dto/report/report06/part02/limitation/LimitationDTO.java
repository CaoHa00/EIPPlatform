package com.EIPplatform.model.dto.report.report06.part02.limitation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LimitationDTO {
    UUID limitationId;
    String limitationName;
    String limitationDescription;
    String improvementPlan;
}