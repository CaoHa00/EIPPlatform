package com.EIPplatform.model.dto.report.report;

import com.EIPplatform.model.dto.report.reporttype.ReportTypeDTO;
import com.EIPplatform.model.dto.report.reportfield.ReportFieldDTO;
import com.EIPplatform.model.dto.report.reportfile.ReportFileDTO;
import com.EIPplatform.model.dto.report.reportsection.ReportSectionDTO;
import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO implements Serializable {

    private UUID reportId;
    private String reportCode;
    private ReportTypeDTO reportType;
    private Integer reportYear;
    private String reportingPeriod;
    private ReportStatusDTO reportStatus;

    private UUID businessDetailId;
    private String businessName;
    private String taxCode;

    private UUID submittedById;
    private String submittedByEmail;
    private String submittedByName;
    private LocalDateTime submittedAt;

    private UUID reviewedById;
    private String reviewedByEmail;
    private String reviewedByName;
    private LocalDateTime reviewedAt;
    private String reviewNotes;

    private BigDecimal completionPercentage;
    private Integer version;
    private UUID parentReportId;

    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ReportFieldDTO> fields;
    private List<ReportSectionDTO> sections;
    private List<ReportFileDTO> files;
}
