package com.EIPplatform.model.dto.report.wastemanagement.exportedhwstat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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