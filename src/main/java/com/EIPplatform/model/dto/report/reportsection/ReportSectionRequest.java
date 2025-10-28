package com.EIPplatform.model.dto.report.reportsection;

import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteRequest;
import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportSectionRequest implements Serializable {

    private UUID sectionId;

    @NotBlank(message = "FIELD_REQUIRED")
    private String sectionType;

    private String sectionData;

    private List<MonitorExceedanceRequest> exceedances;

    private List<HazardWasteRequest> hazardWastes;
}
