package com.EIPplatform.model.dto.report.report.wastewatermanagerDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoWWQcvnExceedancesDTO implements Serializable {
    Long qcvnExceedId;
    String paramName;
    Integer exceedDaysCount;
    BigDecimal qcvnLimitValue;
    BigDecimal exceedRatioPercent;
}
