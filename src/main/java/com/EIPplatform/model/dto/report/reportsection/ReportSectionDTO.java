package com.EIPplatform.model.dto.report.reportsection;
import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteDTO;
import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceDTO;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSectionDTO implements Serializable {
    private UUID sectionId;
    private UUID reportId;
    private String sectionType;
    private String sectionData;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Related data based on section type
    private List<MonitorExceedanceDTO> exceedances;
    private List<HazardWasteDTO> hazardWastes;
}
