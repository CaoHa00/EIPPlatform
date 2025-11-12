package com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndustrialSolidWasteStatUpdateDTO {
    String wasteGroup;

    Double volumeCy;

    String receiverOrg;

    Double volumePy;
}