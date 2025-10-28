package com.EIPplatform.model.dto.report.reportfile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportFileRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 255, message = "MAX_LENGTH_EXCEEDED")
    private String fileName;

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 500, message = "MAX_LENGTH_EXCEEDED")
    private String filePath;

    private Long fileSize;

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 50, message = "MAX_LENGTH_EXCEEDED")
    private String fileType;
}