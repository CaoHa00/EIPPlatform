package com.EIPplatform.service.report;

import java.util.List;

import com.EIPplatform.model.dto.report.ReportStatusDto;

public interface ReportStatusInterface {

     // Tìm theo ID
    ReportStatusDto findById(Long id);

    // Xóa theo ID
    void deleteById(Long id);

    // Các method bổ sung thường dùng
    List<ReportStatusDto> findAll();

    ReportStatusDto create(ReportStatusDto dto);

    ReportStatusDto update(Long id, ReportStatusDto dto);
}
