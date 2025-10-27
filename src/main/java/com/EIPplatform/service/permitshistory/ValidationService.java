package com.EIPplatform.service.permitshistory;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

public interface ValidationService {

    /**
     * Validate email format
     */
    boolean validateEmail(String email);

    /**
     * Validate phone number (Vietnamese format)
     */
    boolean validatePhoneNumber(String phoneNumber);

    /**
     * Validate mã số thuế
     */
    boolean validateTaxCode(String taxCode);

    /**
     * Validate mã CTNH
     */
    boolean validateHwCode(String hwCode);

    /**
     * Validate coordinates
     */
    boolean validateCoordinates(String longitude, String latitude);

    /**
     * Validate file type
     */
    boolean validateFileType(MultipartFile file, List<String> allowedTypes);

    /**
     * Validate file size
     */
    boolean validateFileSize(MultipartFile file, long maxSizeInBytes);

    /**
     * Validate date range
     */
    boolean validateDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Validate year
     */
    boolean validateYear(Integer year);

    /**
     * Sanitize input string
     */
    String sanitizeInput(String input);
}