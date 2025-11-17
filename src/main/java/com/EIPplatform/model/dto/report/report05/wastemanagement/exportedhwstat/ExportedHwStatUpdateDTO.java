package com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat;

import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportedHwStatUpdateDTO {
    String wasteName;

    String hwCode;

    String baselCode;

    Double volumeKg;

    String transporterOrg;

    String overseasProcessorOrg;
}