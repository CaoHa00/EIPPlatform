package com.EIPplatform.service.report.reporta05.airemmissionmanagement;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface AirEmissionDataService {
    /**
     * Tạo AirEmissionData mới từ request và lưu vào draft cache
     */
    AirEmissionDataDTO createAirEmissionData(UUID reportId, AirEmissionDataCreateDTO request, MultipartFile file);

    /**
     * Lấy AirEmissionData từ draft cache
     */
    AirEmissionDataDTO getAirEmissionData(UUID reportId);

    /**
     * Xóa AirEmissionData khỏi draft cache
     */
    void deleteAirEmissionData(UUID reportId);

    /**
     * Upload map file for AirEmissionData (separate endpoint for file-only upload)
     */
//    AirEmissionDataDTO uploadAirEmissionDataFile(UUID reportId, MultipartFile file);

    /**
     * Delete map file for AirEmissionData (separate endpoint)
     */
    void deleteAirEmissionDataFile(UUID reportId);

    /**
     * Download map file for AirEmissionData
     */
    Resource downloadAirEmissionDataFile(UUID reportId);

    /**
     * Check if map file exists for AirEmissionData
     */
    boolean hasAirEmissionDataFile(UUID reportId);
}