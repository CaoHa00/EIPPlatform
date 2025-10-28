package com.EIPplatform.service.report.reportstatus;

import com.EIPplatform.mapper.report.ReportStatusMapper;
import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import com.EIPplatform.model.entity.report.ReportStatus;
import com.EIPplatform.repository.report.reportstatus.ReportStatusRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportStatusImplementation implements ReportStatusInterface {
    private final ReportStatusRepository repository;
    private final ReportStatusMapper mapper;
    
    @Override
    @Transactional(readOnly = true)
    public ReportStatusDTO findById(Long id) {
        ReportStatus entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReportStatus not found with id: " + id));
        return mapper.toDTO(entity);
    }
    
    @Override
    public void deleteById(Long id) {
        // Kiểm tra tồn tại trước khi xóa
        if (!repository.existsById(id)) {
            throw new RuntimeException("ReportStatus not found with id: " + id);
        }
        repository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReportStatusDTO> findAll() {
        List<ReportStatus> entities = repository.findAll();
        return mapper.toDTOList(entities);
    }
    
    @Override
    public ReportStatusDTO create(ReportStatusDTO dto) {
        // Kiểm tra tên đã tồn tại chưa
        if (repository.existsByReportStatusName(dto.getReportStatusName())) {
            throw new RuntimeException("ReportStatus name already exists: " + dto.getReportStatusName());
        }
        
        ReportStatus entity = mapper.toEntity(dto);
        ReportStatus savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }
    
    @Override
    public ReportStatusDTO update(Long id, ReportStatusDTO dto) {
        ReportStatus existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReportStatus not found with id: " + id));
        
        // Kiểm tra tên mới có trùng với record khác không
        if (!existingEntity.getReportStatusName().equals(dto.getReportStatusName()) 
                && repository.existsByReportStatusName(dto.getReportStatusName())) {
            throw new RuntimeException("ReportStatus name already exists: " + dto.getReportStatusName());
        }
        
        existingEntity.setReportStatusName(dto.getReportStatusName());
        ReportStatus updatedEntity = repository.save(existingEntity);
        return mapper.toDTO(updatedEntity);
    }
}
