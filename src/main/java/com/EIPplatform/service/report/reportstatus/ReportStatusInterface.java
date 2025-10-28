package com.EIPplatform.service.report.reportstatus;

import java.util.List;

import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;

public interface ReportStatusInterface {

     // Tìm theo ID
    ReportStatusDTO findById(Long id);
    
    // Xóa theo ID
    void deleteById(Long id);
    
    // Các method bổ sung thường dùng
    List<ReportStatusDTO> findAll();
    
    ReportStatusDTO create(ReportStatusDTO dto);
    
    ReportStatusDTO update(Long id, ReportStatusDTO dto);
}
