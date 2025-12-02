package com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecycleIndustrialWasteStatUpdateDTO {

    String transferOrg;

    Double volumeCy;

    String unitCy;

    String wasteTypeDesc;

    Double volumePy;

    String unitPy;
}