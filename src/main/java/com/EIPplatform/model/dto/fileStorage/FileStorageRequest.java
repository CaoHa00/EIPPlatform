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
    
    /**
     * Tạo đường dẫn folder theo cấu trúc EIP/BusinessName/Sector
     */
    public String toFolderPath() {
        validateFields();
        return String.format("EIP/%s/%s", 
            sanitizePath(businessName), 
            sanitizePath(sector));
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
        
        // Kiểm tra path traversal
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
