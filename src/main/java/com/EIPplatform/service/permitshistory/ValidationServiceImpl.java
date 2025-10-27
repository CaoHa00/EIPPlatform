package com.EIPplatform.service.permitshistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class ValidationServiceImpl implements ValidationService {

    private static final String VIETNAM_PHONE_REGEX = "^(0|\\+84)[0-9]{9}$";
    private static final String TAX_CODE_REGEX = "^[0-9]{10}(-[0-9]{3})?$";
    private static final String HW_CODE_REGEX = "^[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final Set<String> VALID_HW_CODES = Set.of(
            "01.01.01", "01.01.02", "01.02.01", "02.01.01", "02.01.02",
            "03.01.01", "03.02.01", "04.01.01", "05.01.01", "06.01.01",
            "07.01.01", "08.01.01", "09.01.01", "10.01.01", "11.01.01"
    );

    @Override
    public boolean validateEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    @Override
    public boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches(VIETNAM_PHONE_REGEX);
    }

    @Override
    public boolean validateTaxCode(String taxCode) {
        return taxCode != null && taxCode.matches(TAX_CODE_REGEX);
    }

    @Override
    public boolean validateHwCode(String hwCode) {
        return hwCode != null && hwCode.matches(HW_CODE_REGEX) && VALID_HW_CODES.contains(hwCode);
    }

    @Override
    public boolean validateCoordinates(String longitude, String latitude) {
        try {
            double lon = Double.parseDouble(longitude);
            double lat = Double.parseDouble(latitude);
            return lon >= -180 && lon <= 180 && lat >= -90 && lat <= 90;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validateFileType(MultipartFile file, List<String> allowedTypes) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        return contentType != null && allowedTypes.contains(contentType);
    }

    @Override
    public boolean validateFileSize(MultipartFile file, long maxSizeInBytes) {
        return file != null && !file.isEmpty() && file.getSize() <= maxSizeInBytes;
    }

    @Override
    public boolean validateDateRange(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null &&
                (startDate.isBefore(endDate) || startDate.isEqual(endDate));
    }

    @Override
    public boolean validateYear(Integer year) {
        if (year == null) {
            return false;
        }
        int currentYear = LocalDate.now().getYear();
        return year >= 2000 && year <= currentYear + 1;
    }

    @Override
    public String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replaceAll("<script>", "")
                .replaceAll("</script>", "")
                .replaceAll("javascript:", "")
                .replaceAll("onerror=", "")
                .replaceAll("onload=", "")
                .replaceAll("'", "''")
                .trim();
    }
}