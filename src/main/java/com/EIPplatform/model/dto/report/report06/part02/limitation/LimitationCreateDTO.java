package com.EIPplatform.model.dto.report.report06.part02.limitation;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LimitationCreateDTO {
    String limitationName;
    String limitationDescription;
    String improvementPlan;
}