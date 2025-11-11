package com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SelfTreatedHwStatUpdateDTO {
    String wasteName;

    String hwCode;

    BigDecimal volumeKg;

    String selfTreatmentMethod;
}