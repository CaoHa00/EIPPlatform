package com.EIPplatform.model.dto.report.report;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteWaterDataDTO {
    
    Long wwId;
    
   
    
    // ============= NƯỚC THẢI SINH HOẠT =============
    String treatmentWwDesc;
    
    @NotNull(message = "Domestic wastewater current year is required")
    @PositiveOrZero(message = "Domestic wastewater current year must be >= 0")
    Double domWwCy;
    
    @NotNull(message = "Domestic wastewater previous year is required")
    @PositiveOrZero(message = "Domestic wastewater previous year must be >= 0")
    Double domWwPy;
    
    @NotNull(message = "Domestic wastewater design is required")
    @PositiveOrZero(message = "Domestic wastewater design must be >= 0")
    Double domWwDesign;
    
    // ============= NƯỚC THẢI CÔNG NGHIỆP =============
    @NotNull(message = "Industrial wastewater current year is required")
    @PositiveOrZero(message = "Industrial wastewater current year must be >= 0")
    Double industrialWwCy;
    
    @NotNull(message = "Industrial wastewater previous year is required")
    @PositiveOrZero(message = "Industrial wastewater previous year must be >= 0")
    Double industrialWwPy;
    
    @NotNull(message = "Industrial wastewater design is required")
    @PositiveOrZero(message = "Industrial wastewater design must be >= 0")
    Double industrialWwDesign;
    
    // ============= NƯỚC LÀM MÁT =============
    @PositiveOrZero(message = "Cooling water current year must be >= 0")
    Double coolingWaterCy;
    
    @PositiveOrZero(message = "Cooling water previous year must be >= 0")
    Double coolingWaterPy;
    
    @PositiveOrZero(message = "Cooling water design must be >= 0")
    Double coolingWaterDesign;
    
    // ============= TÌNH HÌNH ĐẤU NỐI =============
    @NotNull(message = "Connection status description is required")
    String connectionStatusDesc;
    
    String connectionDiagram;
}