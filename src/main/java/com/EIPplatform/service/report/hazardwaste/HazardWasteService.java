package com.EIPplatform.service.report.hazardwaste;

import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteDTO;
import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HazardWasteService {
    List<HazardWasteDTO> findAll();

    Optional<HazardWasteDTO> findById(Integer id);

    List<HazardWasteDTO> findBySectionId(UUID sectionId);

    List<HazardWasteDTO> findByReportId(UUID reportId);

    HazardWasteDTO createHazardWaste(HazardWasteRequest request, UUID sectionId);

    HazardWasteDTO updateHazardWaste(Integer id, HazardWasteRequest request);

    void deleteById(Integer id);
}