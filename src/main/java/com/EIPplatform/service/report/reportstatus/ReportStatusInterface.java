package com.EIPplatform.service.report.reportstatus;

import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import java.util.List;

public interface ReportStatusInterface {

    ReportStatusDTO findById(Integer id);

    ReportStatusDTO findByCode(String statusCode);

    void deleteById(Integer id);

    List<ReportStatusDTO> findAll();

    ReportStatusDTO create(ReportStatusDTO dto);

    ReportStatusDTO update(Integer id, ReportStatusDTO dto);
}