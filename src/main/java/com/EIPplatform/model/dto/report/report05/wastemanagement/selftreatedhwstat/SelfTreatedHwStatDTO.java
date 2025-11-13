package com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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