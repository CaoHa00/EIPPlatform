package com.EIPplatform.model.dto.fileStorage;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStorageRequest {
    private String businessName;
    private String sector;
    private Integer year;  // Thêm field mới: Năm (optional, ví dụ 2025)

    /**
     * Tạo đường dẫn folder theo cấu trúc EIP/BusinessName/[Year/]Sector
     * Nếu year null, bỏ qua year folder.
     */
    public String toFolderPath() {
        validateFields();
        String path = String.format("EIP/%s", sanitizePath(businessName));

        // Thêm year nếu có
        if (year != null) {
            path += "/" + sanitizePath(year.toString());  // "2025" → "2025"
        }

        path += "/" + sanitizePath(sector);
        return path;
    }

    /**
     * Làm sạch path, chỉ giữ ký tự an toàn
     */
    private String sanitizePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path component cannot be null or empty");
        }
        // Chỉ giữ chữ cái, số, gạch ngang và gạch dưới
        String sanitized = path.trim().replaceAll("[^a-zA-Z0-9-_]", "_");

        // Tránh path traversal
        sanitized = sanitized.replaceAll("\\.\\.", "_");

        return sanitized;
    }

    /**
     * Validate các trường bắt buộc
     */
    public void validateFields() {
        if (!StringUtils.hasText(businessName)) {
            throw new IllegalArgumentException("Business name is required");
        }
        if (!StringUtils.hasText(sector)) {
            throw new IllegalArgumentException("Sector is required");
        }

        // Validate year nếu có
        if (year != null) {
            if (year < 1900 || year > 2100) {  // Giới hạn hợp lý cho năm
                throw new IllegalArgumentException("Year must be between 1900 and 2100");
            }
            // Kiểm tra path traversal cho year (dù khó xảy ra)
            if (year.toString().contains("..") || year.toString().contains("/") || year.toString().contains("\\")) {
                throw new SecurityException("Invalid year: contains illegal characters");
            }
        }

        // Kiểm tra path traversal cho businessName và sector
        if (businessName.contains("..") || businessName.contains("/") || businessName.contains("\\")) {
            throw new SecurityException("Invalid business name: contains illegal characters");
        }
        if (sector.contains("..") || sector.contains("/") || sector.contains("\\")) {
            throw new SecurityException("Invalid sector: contains illegal characters");
        }

        // Kiểm tra độ dài
        if (businessName.length() > 100) {
            throw new IllegalArgumentException("Business name too long (max 100 characters)");
        }
        if (sector.length() > 100) {
            throw new IllegalArgumentException("Sector too long (max 100 characters)");
        }
    }

    /**
     * Tạo full path với file name
     */
    public String toFullPath(String fileName) {
        return toFolderPath() + "/" + fileName;
    }
}