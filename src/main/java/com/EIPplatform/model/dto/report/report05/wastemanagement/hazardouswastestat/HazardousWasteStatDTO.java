package com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;

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