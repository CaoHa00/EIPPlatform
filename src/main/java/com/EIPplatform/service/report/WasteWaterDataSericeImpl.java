package com.EIPplatform.service.report;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.EIPplatform.mapper.report.WasteWaterDataMapper;
import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.ReportA05DraftDTO;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.repository.report.WasteWaterRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class WasteWaterDataSericeImpl implements WasteWaterDataService {
     
    WasteWaterRepository wasteWaterDataRepository;
    ReportA05Repository reportA05Repository;
    WasteWaterDataMapper wasteWaterDataMapper;
    ReportCacheService  reportCacheService;
    @Override
    public void updateWasteWaterDataToCache(UUID reportId, WasteWaterDataDTO dto) {
        log.info("Updating waste water data to CACHE for report: {}", reportId);

        //checking the report is exit or not

        try {
            if(!reportA05Repository.existsById(reportId)){
                throw new Exception("null");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        dto.setReportId(reportId);
        reportCacheService.updateWasteWaterData(reportId, dto);
        log.info("Waste water data updated to CACHE successfully for report: {}", reportId);
    }
    @Override
    public WasteWaterDataDTO getWasteWaterDataFromCache(UUID reportId) {
        log.info("Getting waste water data from CACHE for report: {}", reportId);
        
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        
        if (draft == null || draft.getWasteWaterData() == null) {
            log.warn("Waste water data not found in CACHE for report: {}", reportId);
            return null;
        }
        
        return draft.getWasteWaterData();
    }
    @Override
    public void saveReportFromCacheToDatabase(UUID reportId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveReportFromCacheToDatabase'");
    }
    @Override
    public WasteWaterDataDTO getWasteWaterDataFromDatabase(UUID reportId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWasteWaterDataFromDatabase'");
    }
    @Override
    public void deleteDraftFromCache(UUID reportId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteDraftFromCache'");
    }
  

    
    
}
