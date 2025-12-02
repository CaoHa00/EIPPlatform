package com.EIPplatform.model.dto.businessInformation.process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResponseDto {
    UUID processId;
    UUID businessDetailId;
    String processName;
    String processDescription;
    String processFlowchart;
    LocalDateTime createdAt;
}
