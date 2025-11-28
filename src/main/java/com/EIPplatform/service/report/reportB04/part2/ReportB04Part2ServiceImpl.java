package com.EIPplatform.service.report.reportB04.part2;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.BusinessDetailError;
import com.EIPplatform.exception.errorCategories.LegalRepresentativeError;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.businessInformation.ProductMapper;
import com.EIPplatform.mapper.report.reportB04.part1.ReportInvestorDetailMapper;
import com.EIPplatform.model.dto.businessInformation.products.ProductCreationListRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductCreationRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductListDTO;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.products.Product;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.businessInformation.ProductRepository;
import com.EIPplatform.repository.report.reportB04.ReportB04Repository;
import com.EIPplatform.service.report.reportCache.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.ReportCacheService;
import com.EIPplatform.utils.StringNormalizerUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportB04Part2ServiceImpl implements ReportB04Part2Service {

    ReportB04Repository reportB04Repository;
    ExceptionFactory exceptionFactory;
    BusinessDetailRepository businessDetailRepository;
    ProductMapper productMapper;
    ProductRepository productRepository;
    // Field khởi tạo trong @PostConstruct
    ReportCacheService<ReportB04DraftDTO> reportCacheService;

    @Autowired
    public ReportB04Part2ServiceImpl(
            ReportB04Repository reportB04Repository,
            BusinessDetailRepository businessDetailRepository,
            ReportInvestorDetailMapper reportInvestorDetailMapper,
            ExceptionFactory exceptionFactory,
            ReportCacheFactory reportCacheFactory, ProductMapper productMapper,
            ProductRepository productRepository) {

        this.reportB04Repository = reportB04Repository;
        this.businessDetailRepository = businessDetailRepository;
        this.exceptionFactory = exceptionFactory;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.reportCacheService = reportCacheFactory.getCacheService(ReportB04DraftDTO.class);
    }

    @Override
    @Transactional
    public ProductListDTO createReportB04Part2(UUID reportB04Id,
            UUID businessDetailId,
            ProductCreationListRequest request) {

        // Normalize toàn bộ request
        request = StringNormalizerUtil.normalizeRequest(request);
       

        // 1. Lấy Report
        ReportB04 report = reportB04Repository.findById(reportB04Id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                "ReportB04", "reportB04Id", reportB04Id, ReportError.REPORT_NOT_FOUND));

        // 2. Convert từng item → entity
        List<Product> entities = productMapper.toEntityListFromCreate(request.getProducts());

        // 3. Validate businessDetail + set quan hệ
        BusinessDetail businessDetail = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                "BusinessDetail", "businessDetailId", businessDetailId,
                BusinessDetailError.BUSINESS_DETAIL_ID_NOT_FOUND));
        for (int i = 0; i < entities.size(); i++) {
            Product entity = entities.get(i);
            ProductCreationRequest item = request.getProducts().get(i);
            entity.setReportB04(report);
            entity.setBusinessDetail(businessDetail);
        }

        // 4. Lưu DB
        List<Product> savedList = productRepository.saveAll(entities);

        // 5. Map → DTO list
        ProductListDTO responseList = productMapper.toDTOList(savedList);

        // 6. Ghi cache
        reportCacheService.updateSectionData(
                reportB04Id,
                businessDetailId,
                responseList,
                "Products");

        log.info("Created product list for reportB04 part 2 - reportB04Id: {}, businessDetailId: {}",
                reportB04Id, businessDetailId);

        return responseList;
    }

    @Override
    @Transactional(readOnly = true) // hàm này lấy tiếp dữ liệu từ cache report
    public ProductListDTO getReportB04Part2(UUID reportId, UUID businessDetailId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);
        if (draft == null || draft.getProducts() == null) { // nếu chưa có trong cache thì lấy dữ liệu ban đầu từ business detail
            return getInitialReportB04Part2(businessDetailId);
        }
        return draft.getProducts();

    }

    private ProductListDTO getInitialReportB04Part2(UUID businessDetailId) {
        BusinessDetail businessDetail = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                "BusinessDetail",
                "id",
                businessDetailId,
                LegalRepresentativeError.BUSINESS_DETAIL_NOT_FOUND));

        ProductListDTO productListDTOs = productMapper.toDTOList(businessDetail.getProducts());
        return productListDTOs;
    }

}
