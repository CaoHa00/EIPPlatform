package com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportedHwStatDTO {
    Long exportedId;
    String wasteName;
    String hwCode;
    String baselCode;
    Double volumeKg;
    String transporterOrg;
    String overseasProcessorOrg;
}