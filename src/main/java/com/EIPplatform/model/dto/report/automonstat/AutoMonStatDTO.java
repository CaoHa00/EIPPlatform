package com.EIPplatform.model.dto.report.automonstat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoMonStatDTO implements Serializable {
    private Integer statId;
    private UUID reportId;
    private String paramName;
    private Integer valDesign;
    private Integer valReceived;
    private Integer valError;
    private BigDecimal ratioReceivedDesign;
    private BigDecimal ratioErrorReceived;
}