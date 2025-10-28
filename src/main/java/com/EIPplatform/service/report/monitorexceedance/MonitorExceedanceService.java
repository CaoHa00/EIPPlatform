package com.EIPplatform.service.report.monitorexceedance;

import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceDTO;
import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MonitorExceedanceService {
    List<MonitorExceedanceDTO> findAll();

    Optional<MonitorExceedanceDTO> findById(Integer id);

    List<MonitorExceedanceDTO> findBySectionId(UUID sectionId);

    List<MonitorExceedanceDTO> findByReportId(UUID reportId);

    List<MonitorExceedanceDTO> findBySectionIdAndDate(UUID sectionId, LocalDate date);

    MonitorExceedanceDTO createMonitorExceedance(MonitorExceedanceRequest request, UUID sectionId);

    MonitorExceedanceDTO updateMonitorExceedance(Integer id, MonitorExceedanceRequest request);

    void deleteById(Integer id);
}