package com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportedHwStatDTO {
    Long exportedId;
    String wasteName;
    String hwCode;
    String baselCode;
    Double volume;
    String unit;
    String transporterOrg;
    String overseasProcessorOrg;
}
