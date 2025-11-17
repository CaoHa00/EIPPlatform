package com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HazardousWasteStatUpdateDTO {
    String wasteName;

    String hwCode;

    Double volumeCy;

    String treatmentMethod;

    String receiverOrg;

    Double volumePy;
}