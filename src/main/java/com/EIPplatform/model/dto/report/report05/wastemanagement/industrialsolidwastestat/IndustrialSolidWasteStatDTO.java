package com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndustrialSolidWasteStatDTO {
    Long industrialId;
    String wasteGroup;
    Double volumeCy;
    String receiverOrg;
    Double volumePy;
}