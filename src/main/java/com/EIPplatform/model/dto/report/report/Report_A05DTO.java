package com.EIPplatform.model.dto.report.report;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Report_A05DTO implements Serializable {

     
    UUID reportId;
    
    String reportCode;
    
    @NotNull(message = "Business detail ID is required")
    UUID bussinessDetailId; // Theo tên field trong entity
    
    String companyName; // For display
    
    @NotNull(message = "Report year is required")
    @Min(value = 2000, message = "Report year must be >= 2000")
    @Max(value = 2100, message = "Report year must be <= 2100")
    Integer reportYear;
    
    String reportingPeriod;
    
    // Tạm thời dùng String cho status_id, sau này có thể đổi sang enum
    String status;
    
    UUID submittedById;
    String submittedByName;
    LocalDateTime submittedAt;
    
    UUID reviewedById;
    String reviewedByName;
    LocalDateTime reviewedAt;
    
    String reviewNotes;
    
    BigDecimal completionPercentage;
    
    Integer version;
    
    Boolean isDeleted;
    
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    
    // Nested data
    WasteWaterDataDTO wasteWaterData;

//     private List<ReportFieldDTO> fields;
//     private List<ReportSectionDTO> sections;
//     private List<ReportFileDTO> files;
}
