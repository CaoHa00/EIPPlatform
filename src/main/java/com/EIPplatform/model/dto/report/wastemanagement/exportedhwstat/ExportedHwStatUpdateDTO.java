package com.EIPplatform.model.dto.report.wastemanagement.exportedhwstat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportedHwStatUpdateDTO {
    String wasteName;

    String hwCode;

    String baselCode;

    BigDecimal volumeKg;

    String transporterOrg;

    String overseasProcessorOrg;
}