package com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat;

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
public class HazardousWasteStatDTO {
    Long hazardousId;
    String wasteName;
    String hwCode;
    Double volumeCy;
    String treatmentMethod;
    String receiverOrg;
    Double volumePy;
}