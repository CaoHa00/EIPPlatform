package com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SelfTreatedHwStatDTO {
    Long selfTreatedId;
    String wasteName;
    String hwCode;
    Double volumeKg;
    String selfTreatmentMethod;
}