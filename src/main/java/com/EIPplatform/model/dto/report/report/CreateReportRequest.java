package com.EIPplatform.model.dto.report.report;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReportRequest implements Serializable {
    // @NotNull(message = "Business detail ID is required")
    // check lại nếu sau này có businesDetail thì sẽ bỏ ra vì đang test
    UUID businessDetailId;
    
    @NotNull(message = "Report year is required")
    Integer reportYear;
    
    String reportingPeriod;
}
