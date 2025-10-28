package com.EIPplatform.service.report.wastestat;


import com.EIPplatform.model.dto.report.wastestat.WasteStatDTO;
import com.EIPplatform.model.dto.report.wastestat.WasteStatRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WasteStatService {
    List<WasteStatDTO> findAll();

    Optional<WasteStatDTO> findById(Integer id);

    List<WasteStatDTO> findByReportId(UUID reportId);

    List<WasteStatDTO> findByReportIdAndWasteType(UUID reportId, String wasteType);

    WasteStatDTO createWasteStat(WasteStatRequest request, UUID reportId);

    WasteStatDTO updateWasteStat(Integer id, WasteStatRequest request);

    void deleteById(Integer id);
}