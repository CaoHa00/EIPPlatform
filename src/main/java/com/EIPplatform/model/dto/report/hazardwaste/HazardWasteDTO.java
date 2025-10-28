package com.EIPplatform.model.dto.report.hazardwaste;

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
public class HazardWasteDTO implements Serializable {
    private Integer hwStatId;
    private UUID sectionId;
    private UUID reportId;
    private String wasteName;
    private String hwCode;
    private BigDecimal volumeCy;
    private String treatmentMethod;
    private String receiverOrg;
    private BigDecimal volumePy;
    private String sectionType;
}