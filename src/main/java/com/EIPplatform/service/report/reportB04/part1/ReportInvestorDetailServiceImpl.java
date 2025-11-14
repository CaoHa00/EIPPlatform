package com.EIPplatform.service.report.reportB04.part1;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.mapper.report.reportB04.part1.ReportInvestorDetailMapper;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailUpdateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.response.ReportInvestorDetailResponse;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;
import com.EIPplatform.model.entity.user.investors.Investor;
import com.EIPplatform.model.entity.user.legalDoc.LegalDoc;
import com.EIPplatform.model.entity.report.reportB04.part01.ThirdPartyImplementer;

// Giả định bạn đã có các Repository này
import com.EIPplatform.repository.report.reportB04.part1.ThirdPartyImplementerRepository;
import com.EIPplatform.repository.user.InvestorRepository;
import com.EIPplatform.repository.user.LegalDocRepository;
import com.EIPplatform.repository.user.ReportInvestorDetailRepository;

import jakarta.persistence.EntityNotFoundException; // Dùng exception chuẩn
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor // Tự động tiêm (inject) các trường 'final'
@Transactional // Nên thêm Transactional cho các Service
public class ReportInvestorDetailServiceImpl implements ReportInvestorDetailService {
    
    private final ReportInvestorDetailMapper reportMapper; 
    
    private final ReportInvestorDetailRepository reportRepository;
    
    private final InvestorRepository investorRepository;
    private final LegalDocRepository legalDocRepository;
    private final ThirdPartyImplementerRepository thirdPartyImplementerRepository;

    @Override
    public ReportInvestorDetailResponse create(ReportInvestorDetailCreateRequest request) {

        ReportInvestorDetail entity = reportMapper.toEntityFromCreate(request);
        Investor investor = investorRepository.findById(request.getInvestorId()) 
                .orElseThrow(() -> new EntityNotFoundException("Investor not found: " + request.getInvestorId()));
        entity.setInvestor(investor);

        // LegalDoc là tùy chọn
        if (request.getLegalDocId() != null) {
            LegalDoc legalDoc = legalDocRepository.findById(request.getLegalDocId()) 
                    .orElseThrow(() -> new EntityNotFoundException("LegalDoc not found: " + request.getLegalDocId()));
            entity.setLegalDoc(legalDoc);
        }

        // ThirdPartyImplementer là tùy chọn
        if (request.getThirdPartyImplementerId() != null) {
            ThirdPartyImplementer tpi = thirdPartyImplementerRepository.findById(request.getThirdPartyImplementerId()) 
                    .orElseThrow(() -> new EntityNotFoundException("ThirdPartyImplementer not found: " + request.getThirdPartyImplementerId()));
            entity.setThirdPartyImplementer(tpi);
        }
        ReportInvestorDetail savedEntity = reportRepository.save(entity);

        return reportMapper.toResponse(savedEntity);
    }

    // --- HÀM UPDATE ĐÚNG LOGIC ---
    @Override
    public ReportInvestorDetailResponse update(Long id, ReportInvestorDetailUpdateRequest request) {
        ReportInvestorDetail existingEntity = findEntityById(id);

        reportMapper.updateEntityFromUpdate(request, existingEntity);
        if (request.getInvestorId() != null) {
            Investor investor = investorRepository.findById(request.getInvestorId()) 
                    .orElseThrow(() -> new EntityNotFoundException("Investor not found: " + request.getInvestorId()));
            existingEntity.setInvestor(investor);
        }
        
        if (request.getLegalDocId() != null) {
            LegalDoc legalDoc = legalDocRepository.findById(request.getLegalDocId())
                    .orElseThrow(() -> new EntityNotFoundException("LegalDoc not found: " + request.getLegalDocId()));
            existingEntity.setLegalDoc(legalDoc);
        }
        
        if (request.getThirdPartyImplementerId() != null) {
            ThirdPartyImplementer tpi = thirdPartyImplementerRepository.findById(request.getThirdPartyImplementerId())
                    .orElseThrow(() -> new EntityNotFoundException("ThirdPartyImplementer not found: " + request.getThirdPartyImplementerId()));
            existingEntity.setThirdPartyImplementer(tpi);
        }

        ReportInvestorDetail savedEntity = reportRepository.save(existingEntity);

        return reportMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional(readOnly = true) 
    public ReportInvestorDetailResponse getById(Long id) {
        return reportRepository.findById(id)
                .map(reportMapper::toResponse) 
                .orElseThrow(() -> new EntityNotFoundException("ReportInvestorDetail not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportInvestorDetailResponse> getAll() {
        return reportMapper.toResponseList(reportRepository.findAll());
    }

    @Override
    public void delete(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new EntityNotFoundException("ReportInvestorDetail not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }

    private ReportInvestorDetail findEntityById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReportInvestorDetail not found with id: " + id));
    }
}