package com.EIPplatform.model.dto.report.wastestat;

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
public class WasteStatDTO implements Serializable {
    private Integer statId;
    private UUID reportId;
    private String wasteType;
    private BigDecimal volumeCy;
    private String receiverOrg;
    private BigDecimal volumePy;
}