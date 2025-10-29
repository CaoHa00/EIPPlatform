package com.EIPplatform.model.dto.permitshistory;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermitStatisticsDTO {
    private Boolean hasEnvPermit;
    private Long totalComponentPermits;
    private Long activeComponentPermits;
    private Long inactiveComponentPermits;
    private Map<String, Long> permitsByType;
}