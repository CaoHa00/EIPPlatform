package com.EIPplatform.service.report.reportB04;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.businessInformation.ProductMapper;
import com.EIPplatform.mapper.report.reportB04.ReportB04Mapper;
import com.EIPplatform.mapper.report.reportB04.part1.ReportInvestorDetailMapper;
import com.EIPplatform.mapper.report.reportB04.part3.ResourcesSavingAndReductionMapper;
import com.EIPplatform.mapper.report.reportB04.part4.SymbiosisIndustryMapper;
import com.EIPplatform.model.dto.businessInformation.products.ProductListDTO;
import com.EIPplatform.model.dto.businessInformation.products.ProductResponse;
import com.EIPplatform.model.dto.report.report05.CreateReportRequest;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DTO;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.ResourcesSavingAndReductionDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.SymbiosisIndustryDTO;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.products.Product;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.businessInformation.ProductRepository;
import com.EIPplatform.repository.report.reportB04.ReportB04Repository;
import com.EIPplatform.repository.report.reportB04.part1.ReportInvestorDetailRepository;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.service.products.ProductInterface;
import com.EIPplatform.service.report.reportB04.part1.ReportInvestorDetailService;
import com.EIPplatform.service.report.reportB04.part2.ReportB04Part2Service;
import com.EIPplatform.service.report.reportCache.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.ReportCacheService;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated // Để enable method-level validation nếu cần
public class ReportB04ServiceImpl implements ReportB04Service {
    // Dependencies inject qua constructor (final)

    ReportB04Mapper reportB04Mapper;
    ReportB04Repository reportB04Repository;
    BusinessDetailRepository businessDetailRepository;
    ReportInvestorDetailRepository reportInvestorDetailRepository;
    ReportInvestorDetailMapper reportInvestorDetailMapper;
    ProductRepository productRepository;
    ProductMapper productMapper;
    ReportB04Part2Service productService;
    ExceptionFactory exceptionFactory;
    FileStorageService fileStorageService;
    ReportCacheFactory reportCacheFactory;
    ResourcesSavingAndReductionMapper resourcesSavingAndReductionMapper;
    SymbiosisIndustryMapper symbiosisIndustryMapper;
    ReportInvestorDetailService reportInvestorDetailService;
    private final ReportCacheService<ReportB04DraftDTO> reportCacheService;

    @Autowired
    public ReportB04ServiceImpl(
            ReportB04Mapper reportB04Mapper,
            ReportB04Repository reportB04Repository,
            BusinessDetailRepository businessDetailRepository,
            ReportInvestorDetailRepository reportInvestorDetailRepository,
            ReportInvestorDetailMapper reportInvestorDetailMapper,
            ProductRepository productRepository,
            ProductMapper productMapper,
            ExceptionFactory exceptionFactory,
            FileStorageService fileStorageService,
            ReportCacheFactory reportCacheFactory,
            ResourcesSavingAndReductionMapper resourcesSavingAndReductionMapper,
            SymbiosisIndustryMapper symbiosisIndustryMapper, ReportInvestorDetailService reportInvestorDetailService,
            ReportB04Part2Service productService) {

        this.reportB04Mapper = reportB04Mapper;
        this.reportB04Repository = reportB04Repository;
        this.businessDetailRepository = businessDetailRepository;
        this.reportInvestorDetailRepository = reportInvestorDetailRepository;
        this.reportInvestorDetailMapper = reportInvestorDetailMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.exceptionFactory = exceptionFactory;
        this.fileStorageService = fileStorageService;
        this.reportCacheFactory = reportCacheFactory;
        this.resourcesSavingAndReductionMapper = resourcesSavingAndReductionMapper;
        this.symbiosisIndustryMapper = symbiosisIndustryMapper;
        this.reportInvestorDetailService = reportInvestorDetailService;
        this.productService = productService;
        // Lấy cache service qua factory
        this.reportCacheService = reportCacheFactory.getCacheService(ReportB04DraftDTO.class);
    }

    @NonFinal
    @Value("${app.storage.local.upload-dir:/app/uploads}")
    String uploadDir;

    @Override
    @Transactional
    public ReportB04DraftDTO createReport(CreateReportRequest request, BusinessDetail businessDetail) {
        String reportCode = "RPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ReportB04 report = ReportB04.builder()
                .reportCode(reportCode)
                .businessDetail(businessDetail)
                .reportYear(request.getReportYear())
                .reportingPeriod(request.getReportingPeriod())
                .version(1)
                .isDeleted(false)
                .completionPercentage(0.0)
                .build();

        ReportB04 saved = reportB04Repository.save(report);
        ReportB04DraftDTO draft = ReportB04DraftDTO.builder()
                .reportId(saved.getReportId())
                .isDraft(true)
                .lastModified(LocalDateTime.now())
                .build();
        reportCacheService.saveDraftReport(draft, request.getBusinessDetailId(), saved.getReportId());
        return draft;
    }

    @Override
    @Transactional
    public ReportB04DTO getOrCreateReportByBusinessDetailId(UUID businessDetailId) {

        // 1. Kiểm tra business (CHỈ KHI CÓ businessDetailId)
        BusinessDetail businessDetail = null;
        if (businessDetailId != null) {
            businessDetail = businessDetailRepository
                    .findById(businessDetailId)
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail",
                            businessDetailId, ReportError.BUSINESS_NOT_FOUND));
        }
        // 1. Fetch basic report
        Optional<ReportB04> optionalReport = reportB04Repository.findByBusinessDetailBusinessDetailId(businessDetailId);
        ReportB04 reportB04 = optionalReport.orElse(null);
        ReportB04DraftDTO draft = new ReportB04DraftDTO();
        if (optionalReport.isEmpty()) { // chưa có trong db chắc chắn chưa tạo 
            draft = createReport(
                    CreateReportRequest.builder()
                            .businessDetailId(businessDetailId)
                            .reportYear(LocalDateTime.now().getYear())
                            .reportingPeriod("ANNUAL")
                            .build(),
                    businessDetail);
        } else {
            if (reportCacheService.getDraftReport(draft.getReportId(), businessDetailId) == null) { // nếu có trong db
                                                                                                    // mà chưa có trong
                                                                                                    // cache
                UUID reportId = reportB04.getReportId();
                draft.setReportId(reportId);
                draft.setReportInvestorDetail(
                        reportInvestorDetailService.getReportInvestorDetailDTO(reportId, businessDetailId));
                draft.setProducts(productService.getReportB04Part2(reportId, businessDetailId));
                reportCacheService.saveDraftReport(draft, businessDetailId, reportId);
            }
            ;
        }
        return ReportB04DTO.builder()
                .reportId(draft.getReportId())
                .reportCode(draft.getReportCode())
                .businessDetailId(businessDetailId)
                .facilityName(businessDetail != null
                        ? businessDetail.getFacilityName()
                        : null)
                .reportYear(draft.getReportYear())
                .reportingPeriod(draft.getReportingPeriod())
                .reviewNotes(draft.getReviewNotes())
                .inspectionRemedyReport(draft.getInspectionRemedyReport())
                .completionPercentage(draft.getCompletionPercentage())
                .createdAt(draft.getCreatedAt())
                .build();
    }

    @Override
    public ReportB04DraftDTO getDraftData(UUID reportId, UUID userAccountId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null) {
            return null;
        }
        return draft;
    }

    /**
     * Cập nhật completion percentage cho draft dựa trên dữ liệu hiện tại (Gọi
     * sau mỗi step để tự động tính % và lưu lại cache)
     */
    // @Override
    // @Transactional
    // public ReportA05DraftDTO updateDraftCompletion(UUID reportId, UUID
    // userAccountId) {
    // ReportA05DraftDTO draft = getDraftData(reportId, userAccountId);
    // if (draft == null) {
    // throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
    // }
    // int percentage = calculateCompletionPercentage(draft);
    // draft.setCompletionPercentage(percentage);
    // draft.setLastModified(LocalDateTime.now());
    // reportCacheService.saveDraftReport(draft, userAccountId);
    // log.info("Updated completion for report {} (user {}): {}%", reportId,
    // userAccountId, percentage);
    // return draft;
    // }
    @Override
    @Transactional
    public ReportB04DTO submitDraftToDatabase(UUID reportId, UUID userAccountId) {
        ReportB04DraftDTO draftData = getDraftData(reportId, userAccountId);
        if (draftData == null) {
            throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
        }

        if (draftData.getCompletionPercentage() == null) {
            double percentage = calculateCompletionPercentage(draftData);
            draftData.setCompletionPercentage(percentage);
        }

        if (!isDraftComplete(draftData)) {
            throw exceptionFactory.createValidationException("ReportB04Draft", "completionPercentage",
                    (draftData.getCompletionPercentage() != null
                            ? draftData.getCompletionPercentage()
                            : 0),
                    ReportError.DRAFT_INCOMPLETE);
        }

        ReportB04 report = reportB04Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportB04", reportId,
                        ReportError.REPORT_NOT_FOUND));

        // part 1 update reportInvestorDetail
        saveOrUpdatePart(
                report,
                draftData.getReportInvestorDetail(),
                () -> report.getReportInvestorDetail(),
                reportInvestorDetailMapper::updateEntityFromDto,
                reportInvestorDetailMapper::dtoToEntity,
                (entity, r) -> entity.setReportB04(r),
                ReportB04::setReportInvestorDetail);
        // part 2 update product
        saveOrUpdateProducts(report, draftData);
        // part 3
        saveOrUpdatePart(
                report,
                draftData.getResourcesSavingAndReduction(),
                () -> report.getResourcesSavingAndReduction(),
                resourcesSavingAndReductionMapper::updateEntityFromDto,
                resourcesSavingAndReductionMapper::dtoToEntity,
                (entity, r) -> entity.setReportB04(r),
                ReportB04::setResourcesSavingAndReduction);

        // part 4
        saveOrUpdatePart(
                report,
                draftData.getSymbiosisIndustry(),
                () -> report.getSymbiosisIndustry(),
                symbiosisIndustryMapper::updateEntityFromDto,
                symbiosisIndustryMapper::dtoToEntity,
                (entity, r) -> entity.setReportB04(r),
                ReportB04::setSymbiosisIndustry);

        if (draftData.getCompletionPercentage() != null) {
            report.setCompletionPercentage(Double.valueOf(draftData.getCompletionPercentage()));
        }

        ReportB04 saved = reportB04Repository.save(report);

        draftData.setIsDraft(false);
        draftData.setLastModified(LocalDateTime.now());
        reportCacheService.deleteDraftReport(reportId, userAccountId);

        // part 1
        ReportInvestorDetailDTO reportInvestorDetailDTO = null;
        if (saved.getReportInvestorDetail() != null) {
            reportInvestorDetailDTO = reportInvestorDetailMapper.toDTO(saved.getReportInvestorDetail());
        }

        // part 2
        ProductListDTO products = null;
        if (saved.getProducts() != null) {
            products = ProductListDTO.builder()
                    .products(productMapper.toDTOListFromCreate(saved.getProducts()))
                    .build();
        }
        // part 3
        ResourcesSavingAndReductionDTO resourcesSavingAndReductionDTO = null;
        if (saved.getResourcesSavingAndReduction() != null) {
            resourcesSavingAndReductionDTO = resourcesSavingAndReductionMapper
                    .toDTO(saved.getResourcesSavingAndReduction());
        }

        // part 4
        SymbiosisIndustryDTO symbiosisIndustryDTO = null;
        if (saved.getSymbiosisIndustry() != null) {
            symbiosisIndustryDTO = symbiosisIndustryMapper.toDTO(saved.getSymbiosisIndustry());
        }
        return ReportB04DTO.builder()
                .reportId(saved.getReportId())
                .reportCode(saved.getReportCode())
                .businessDetailId(report.getBusinessDetail() != null
                        ? report.getBusinessDetail().getBusinessDetailId()
                        : null)
                .facilityName(report.getBusinessDetail() != null
                        ? report.getBusinessDetail().getFacilityName()
                        : null)
                .reportYear(saved.getReportYear())
                .reportingPeriod(saved.getReportingPeriod())
                .reviewNotes(saved.getReviewNotes())
                .reportInvestorDetail(reportInvestorDetailDTO)
                .products(products)
                .resourcesSavingAndReduction(resourcesSavingAndReductionDTO)
                .symbiosisIndustry(symbiosisIndustryDTO)
                .inspectionRemedyReport(saved.getInspectionRemedyReport())
                .completionPercentage(saved.getCompletionPercentage())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private void saveOrUpdateProducts(ReportB04 report, ReportB04DraftDTO draftData) {
        // Lấy wrapper DTO
        ProductListDTO dtoWrapper = draftData.getProducts();
        if (dtoWrapper == null || dtoWrapper.getProducts() == null || dtoWrapper.getProducts().isEmpty()) {
            return;
        }

        List<ProductResponse> dtoList = dtoWrapper.getProducts();
        List<Product> existingEntities = report.getProducts();

        if (existingEntities != null && !existingEntities.isEmpty()) {
            // --- Update existing list ---
            for (int i = 0; i < dtoList.size(); i++) {
                ProductResponse dto = dtoList.get(i);
                if (i < existingEntities.size()) {
                    // Partial update entity hiện có
                    Product entity = existingEntities.get(i);
                    productMapper.updateEntityFromDto(dto, entity);
                } else {
                    // Create mới nếu DTO dài hơn list hiện có
                    Product entity = productMapper.toEntity(dto);
                    entity.setReportB04(report);
                    existingEntities.add(entity);
                }
            }
            // Lưu danh sách update trở lại report
            report.setProducts(existingEntities);

        } else {
            // --- Create mới hoàn toàn ---
            List<Product> newEntities = dtoList.stream()
                    .map(dto -> {
                        Product entity = productMapper.toEntity(dto);
                        entity.setReportB04(report);
                        return entity;
                    })
                    .toList();

            report.setProducts(newEntities);
        }

        // Optional: nếu bạn muốn xóa các entity dư thừa khi DTO ngắn hơn list cũ
        if (existingEntities != null && existingEntities.size() > dtoList.size()) {
            List<Product> toRemove = existingEntities.subList(dtoList.size(), existingEntities.size());
            toRemove.forEach(productRepository::delete); // xóa khỏi DB
            existingEntities.subList(dtoList.size(), existingEntities.size()).clear();
        }

        // Cuối cùng: lưu list product
        productRepository.saveAll(report.getProducts());
    }

    // @Override
    // @Transactional
    // public InspectionRemedyResponse updateInspectionRemedyReport(UUID reportId,
    // UpdateInspectionRemedyReportRequest request) {
    // // Validate report tồn tại
    // ReportA05 report = reportA05Repository.findById(reportId)
    // .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05",
    // reportId,
    // ReportError.REPORT_NOT_FOUND));
    // // Validate request không null và trường chính không null (nếu cần, nhưng
    // @Size
    // // đã handle length)
    // if (request == null) {
    // throw
    // exceptionFactory.createValidationException("UpdateInspectionRemedyReportRequest",
    // "request", null,
    // ReportError.INVALID_REQUEST);
    // }
    // if (Objects.isNull(request.getInspectionRemedyReport())) {
    // throw
    // exceptionFactory.createValidationException("UpdateInspectionRemedyReportRequest",
    // "inspectionRemedyReport", null, ReportError.FIELD_REQUIRED);
    // }
    // // Update trường
    // report.setInspectionRemedyReport(request.getInspectionRemedyReport());
    // report.setUpdatedAt(LocalDateTime.now()); // Cập nhật timestamp nếu cần
    // ReportA05 saved = reportA05Repository.save(report);
    // // Log
    // log.info("Updated inspection remedy report for reportId: {}", reportId);
    // // Build và return response mới đơn giản
    // return InspectionRemedyResponse.builder()
    // .reportId(saved.getReportId())
    // .inspectionRemedyReport(saved.getInspectionRemedyReport())
    // .updatedAt(saved.getUpdatedAt())
    // .build();
    // }
    // TODO: later add percentage
    private int calculateCompletionPercentage(ReportB04DraftDTO draft) {
        return 100;
    }

    private boolean isSectionComplete(Object sectionDto) {
        if (sectionDto == null) {
            return false;
        }

        return true;
    }

    private boolean isDraftComplete(ReportB04DraftDTO draftData) {
        return draftData.getReportInvestorDetail() != null
                && draftData.getProducts() != null;
    }

    private <E, D, R> void saveOrUpdatePart(
            R report, // parent entity
            D dto, // DTO tương ứng
            Supplier<E> existingEntityGetter, // lấy entity hiện có
            BiConsumer<D, E> updateMapper, // mapper update từ DTO → entity
            Function<D, E> createMapper, // mapper tạo entity mới từ DTO
            BiConsumer<E, R> setParent, // set report/parent entity vào child
            BiConsumer<R, E> setEntity // gán child vào report
    ) {
        if (dto == null) {
            return;
        }

        E entity = existingEntityGetter.get();
        if (entity != null) {
            updateMapper.accept(dto, entity);
        } else {
            entity = createMapper.apply(dto);
            setParent.accept(entity, report);
        }
        setEntity.accept(report, entity);
    }

    // @Override
    // public byte[] generateReportFile(UUID reportId, UUID userAccountId) throws
    // Exception {
    // ReportA05 report = reportA05Repository.findById(reportId)
    // .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05",
    // reportId,
    // ReportError.REPORT_NOT_FOUND));
    // BusinessDetail business = report.getBusinessDetail();
    // if (business == null) {
    // throw exceptionFactory.createCustomException(ReportError.BUSINESS_NOT_FOUND);
    // }
    // ReportA05DraftDTO draftData = getDraftData(reportId, userAccountId);
    // WasteWaterDataDTO wasteWaterDataDTO = draftData != null ?
    // draftData.getWasteWaterData() : null;
    // AirEmissionDataDTO airEmissionDataDTO = draftData != null ?
    // draftData.getAirEmissionData() : null;
    // WasteManagementDataDTO wasteManagementDataDTO = draftData != null ?
    // draftData.getWasteManagementData()
    // : null;
    // EnvPermits envPermits = business.getEnvPermits();
    // List<BusinessHistoryConsumption> businessHistoryConsumptions = business
    // .getBusinessHistoryConsumptions();
    // LocalDate today = LocalDate.now();
    // String day = String.valueOf(today.getDayOfMonth());
    // String month = String.valueOf(today.getMonthValue());
    // String year = String.valueOf(today.getYear());
    // // FIX: Map dữ liệu với key chính xác
    // Map<String, String> data = new HashMap<>();
    // data.put("facility_name", business.getFacilityName());
    // data.put("address", business.getAddress());
    // data.put("phone_number", business.getPhoneNumber());
    // // data.put("legal_representative", business.getLegalRepresentative());
    // data.put("activity_type", business.getActivityType());
    // data.put("scale_capacity", business.getScaleCapacity());
    // data.put("iso_14001_certificate",
    // business.getISO_certificate_14001() != null ?
    // business.getISO_certificate_14001() : "");
    // data.put("business_license_number",
    // business.getBusinessRegistrationNumber());
    // data.put("tax_code", business.getTaxCode());
    // data.put("seasonal_period", business.getOperationType().name());
    // // permit
    // data.put("env_permit_number", envPermits.getPermitNumber());
    // data.put("env_permit_issue_date", formatDate(envPermits.getIssueDate()));
    // data.put("env_permit_issuer", envPermits.getIssuerOrg());
    // data.put("env_permit_file", envPermits.getPermitFilePath());
    // // business history
    // for (BusinessHistoryConsumption bhc : businessHistoryConsumptions) {
    // data.put("product_volume_cy",
    // bhc.getProductVolumeCy() != null ? bhc.getProductVolumeCy().toString() : "");
    // data.put("product_unit_cy", bhc.getProductUnitCy());
    // data.put("product_volume_py",
    // bhc.getProductVolumePy() != null ? bhc.getProductVolumePy().toString() : "");
    // data.put("product_unit_py", bhc.getProductUnitPy());
    // data.put("fuel_consumption_cy",
    // bhc.getFuelConsumptionCy() != null ? bhc.getFuelConsumptionCy().toString()
    // : "");
    // data.put("fuel_unit_cy", bhc.getFuelUnitCy());
    // data.put("fuel_consumption_py",
    // bhc.getFuelConsumptionPy() != null ? bhc.getFuelConsumptionPy().toString()
    // : "");
    // data.put("fuel_unit_py", bhc.getFuelUnitPy());
    // data.put("electricity_consumption_cy",
    // bhc.getElectricityConsumptionCy() != null
    // ? bhc.getElectricityConsumptionCy().toString()
    // : "");
    // data.put("electricity_consumption_py",
    // bhc.getElectricityConsumptionPy() != null
    // ? bhc.getElectricityConsumptionPy().toString()
    // : "");
    // data.put("water_consumption_cy",
    // bhc.getWaterConsumptionCy() != null ? bhc.getWaterConsumptionCy().toString()
    // : "");
    // data.put("water_consumption_py",
    // bhc.getWaterConsumptionPy() != null ? bhc.getWaterConsumptionPy().toString()
    // : "");
    // }
    // data.put("dateStr", day);
    // data.put("monthYearStr", month);
    // data.put("yearStr", year);
    // if (wasteWaterDataDTO != null) {
    // log.debug("Log wasteWaterDATAdto");
    // data.put("ww_treatment_desc",
    // wasteWaterDataDTO.getTreatmentWwDesc() != null
    // ? wasteWaterDataDTO.getTreatmentWwDesc()
    // : "");
    // // Nước thải sinh hoạt
    // data.put("domestic_ww_cy",
    // wasteWaterDataDTO.getDomWwCy() != null
    // ? wasteWaterDataDTO.getDomWwCy().toString()
    // : "");
    // data.put("domestic_ww_py",
    // wasteWaterDataDTO.getDomWwPy() != null
    // ? wasteWaterDataDTO.getDomWwPy().toString()
    // : "");
    // data.put("domestic_ww_design",
    // wasteWaterDataDTO.getDomWwDesign() != null
    // ? wasteWaterDataDTO.getDomWwDesign().toString()
    // : "");
    // // Nước thải công nghiệp
    // data.put("industrial_ww_cy",
    // wasteWaterDataDTO.getIndustrialWwCy() != null
    // ? wasteWaterDataDTO.getIndustrialWwCy().toString()
    // : "");
    // data.put("industrial_ww_py",
    // wasteWaterDataDTO.getIndustrialWwPy() != null
    // ? wasteWaterDataDTO.getIndustrialWwPy().toString()
    // : "");
    // data.put("industrial_ww_design", wasteWaterDataDTO.getIndustrialWwDesign() !=
    // null
    // ? wasteWaterDataDTO.getIndustrialWwDesign().toString()
    // : "");
    // // Nước làm mát
    // data.put("cooling_water_cy",
    // wasteWaterDataDTO.getCoolingWaterCy() != null
    // ? wasteWaterDataDTO.getCoolingWaterCy().toString()
    // : "");
    // data.put("cooling_water_py",
    // wasteWaterDataDTO.getCoolingWaterPy() != null
    // ? wasteWaterDataDTO.getCoolingWaterPy().toString()
    // : "");
    // data.put("cooling_water_design",
    // wasteWaterDataDTO.getCoolingWaterDesign() != null
    // ? wasteWaterDataDTO.getCoolingWaterDesign().toString()
    // : "");
    // // Tình hình đầu nối hệ thống XLNT tập trung
    // data.put("connection_status_desc",
    // wasteWaterDataDTO.getConnectionStatusDesc() != null
    // ? wasteWaterDataDTO.getConnectionStatusDesc()
    // : "");
    // // kết quả quan trắc nước thải
    // // nước thải sinh hoạt
    // data.put("dom_monitor_period",
    // wasteWaterDataDTO.getDomMonitorPeriod() != null
    // ? wasteWaterDataDTO.getDomMonitorPeriod()
    // : "");
    // data.put("dom_monitor_freq", wasteWaterDataDTO.getDomMonitorFreq() != null
    // ? wasteWaterDataDTO.getDomMonitorFreq()
    // : "");
    // data.put("dom_monitor_locations",
    // wasteWaterDataDTO.getDomMonitorLocations() != null
    // ? wasteWaterDataDTO.getDomMonitorLocations()
    // : "");
    // data.put("dom_sample_count", wasteWaterDataDTO.getDomSampleCount() != null
    // ? wasteWaterDataDTO.getDomSampleCount().toString()
    // : "");
    // data.put("dom_qcvn_standard",
    // wasteWaterDataDTO.getDomQcvnStandard() != null
    // ? wasteWaterDataDTO.getDomQcvnStandard()
    // : "");
    // data.put("dom_agency_name",
    // wasteWaterDataDTO.getDomAgencyName() != null
    // ? wasteWaterDataDTO.getDomAgencyName()
    // : "");
    // data.put("dom_agency_vimcerts",
    // wasteWaterDataDTO.getDomAgencyVimcerts() != null
    // ? wasteWaterDataDTO.getDomAgencyVimcerts()
    // : "");
    // // nước thải công nghiệp
    // data.put("ind_monitor_period",
    // wasteWaterDataDTO.getIndMonitorPeriod() != null
    // ? wasteWaterDataDTO.getIndMonitorPeriod()
    // : "");
    // data.put("ind_monitor_freq",
    // wasteWaterDataDTO.getIndMonitorFreq() != null
    // ? wasteWaterDataDTO.getIndMonitorFreq()
    // : "");
    // data.put("ind_monitor_locations",
    // wasteWaterDataDTO.getIndMonitorLocations() != null
    // ? wasteWaterDataDTO.getIndMonitorLocations()
    // : "");
    // data.put("ind_sample_count",
    // wasteWaterDataDTO.getIndSampleCount() != null
    // ? wasteWaterDataDTO.getIndSampleCount().toString()
    // : "");
    // data.put("ind_qcvn_standard",
    // wasteWaterDataDTO.getIndQcvnStandard() != null
    // ? wasteWaterDataDTO.getIndQcvnStandard()
    // : "");
    // data.put("ind_agency_name",
    // wasteWaterDataDTO.getIndAgencyName() != null
    // ? wasteWaterDataDTO.getIndAgencyName()
    // : "");
    // data.put("ind_agency_vimcerts",
    // wasteWaterDataDTO.getIndAgencyVimcerts() != null
    // ? wasteWaterDataDTO.getIndAgencyVimcerts()
    // : "");
    // // Quan trắc nước thải liên tục tự động (Nếu có
    // // thông tin chung
    // data.put("auto_station_location",
    // wasteWaterDataDTO.getAutoStationLocation() != null
    // ? wasteWaterDataDTO.getAutoStationLocation()
    // : "");
    // data.put("auto_station_GPS",
    // wasteWaterDataDTO.getAutoStationGps() != null
    // ? wasteWaterDataDTO.getAutoStationGps()
    // : "");
    // data.put("auto_station_map",
    // wasteWaterDataDTO.getAutoStationMap() != null
    // ? wasteWaterDataDTO.getAutoStationMap()
    // : "");
    // data.put("auto_source_desc",
    // wasteWaterDataDTO.getAutoSourceDesc() != null
    // ? wasteWaterDataDTO.getAutoSourceDesc()
    // : "");
    // data.put("auto_data_frequency",
    // wasteWaterDataDTO.getAutoDataFrequency() != null
    // ? wasteWaterDataDTO.getAutoDataFrequency()
    // : "");
    // data.put("auto_calibration_info",
    // wasteWaterDataDTO.getAutoCalibrationInfo() != null
    // ? wasteWaterDataDTO.getAutoCalibrationInfo()
    // : "");
    // // tình trangh haot động của trạm
    // data.put("auto_incident_summary",
    // wasteWaterDataDTO.getAutoIncidentSummary() != null
    // ? wasteWaterDataDTO.getAutoIncidentSummary()
    // : "");
    // data.put("auto_downtime_desc",
    // wasteWaterDataDTO.getAutoDowntimeDesc() != null
    // ? wasteWaterDataDTO.getAutoDowntimeDesc()
    // : "");
    // // nhận xét kết quả quan trắc
    // data.put("auto_exceed_days_summary",
    // wasteWaterDataDTO.getAutoExceedDaysSummary() != null
    // ? wasteWaterDataDTO.getAutoExceedDaysSummary()
    // : "");
    // data.put("auto_abnormal_reason",
    // wasteWaterDataDTO.getAutoAbnormalReason() != null
    // ? wasteWaterDataDTO.getAutoAbnormalReason()
    // : "");
    // // kết luận
    // data.put("auto_completeness_review",
    // wasteWaterDataDTO.getAutoCompletenessReview() != null
    // ? wasteWaterDataDTO.getAutoCompletenessReview()
    // : "");
    // data.put("auto_exceed_summary",
    // wasteWaterDataDTO.getAutoExceedSummary() != null
    // ? wasteWaterDataDTO.getAutoExceedSummary()
    // : "");
    // }
    // if (airEmissionDataDTO != null) {
    // data.put("air_treatment_desc",
    // airEmissionDataDTO.getAirTreatmentDesc() != null
    // ? airEmissionDataDTO.getAirTreatmentDesc()
    // : "");
    // data.put("air_emission_cy",
    // airEmissionDataDTO.getAirEmissionCy() != null
    // ? airEmissionDataDTO.getAirEmissionCy().toString()
    // : "");
    // data.put("air_emission_py",
    // airEmissionDataDTO.getAirEmissionPy() != null
    // ? airEmissionDataDTO.getAirEmissionPy().toString()
    // : "");
    // data.put("air_monitor_period",
    // airEmissionDataDTO.getAirMonitorPeriod() != null
    // ? airEmissionDataDTO.getAirMonitorPeriod()
    // : "");
    // data.put("air_monitor_freq", airEmissionDataDTO.getAirMonitorFreq() != null
    // ? airEmissionDataDTO.getAirMonitorFreq()
    // : "");
    // data.put("air_monitor_locations",
    // airEmissionDataDTO.getAirMonitorLocations() != null
    // ? airEmissionDataDTO.getAirMonitorLocations()
    // : "");
    // data.put("air_sample_count", airEmissionDataDTO.getAirSampleCount() != null
    // ? airEmissionDataDTO.getAirSampleCount().toString()
    // : "");
    // data.put("air_qcvn_standard",
    // airEmissionDataDTO.getAirQcvnStandard() != null
    // ? airEmissionDataDTO.getAirQcvnStandard()
    // : "");
    // data.put("air_agency_name",
    // airEmissionDataDTO.getAirAgencyName() != null
    // ? airEmissionDataDTO.getAirAgencyName()
    // : "");
    // data.put("air_agency_vimcerts",
    // airEmissionDataDTO.getAirAgencyVimcerts() != null
    // ? airEmissionDataDTO.getAirAgencyVimcerts()
    // : "");
    // data.put("air_auto_station_location",
    // airEmissionDataDTO.getAirAutoStationLocation() != null
    // ? airEmissionDataDTO.getAirAutoStationLocation()
    // : "");
    // data.put("air_auto_station_GPS",
    // airEmissionDataDTO.getAirAutoStationGps() != null
    // ? airEmissionDataDTO.getAirAutoStationGps()
    // : "");
    // data.put("air_auto_station_map",
    // airEmissionDataDTO.getAirAutoStationMapFilePath() != null
    // ? airEmissionDataDTO.getAirAutoStationMapFilePath()
    // : "");
    // data.put("air_auto_source_desc",
    // airEmissionDataDTO.getAirAutoSourceDesc() != null
    // ? airEmissionDataDTO.getAirAutoSourceDesc()
    // : "");
    // data.put("air_auto_data_frequency",
    // airEmissionDataDTO.getAirAutoDataFrequency() != null
    // ? airEmissionDataDTO.getAirAutoDataFrequency()
    // : "");
    // data.put("air_auto_param_list",
    // airEmissionDataDTO.getAirAutoParamList() != null
    // ? airEmissionDataDTO.getAirAutoParamList()
    // : "");
    // data.put("air_auto_calibration_info",
    // airEmissionDataDTO.getAirAutoCalibrationInfo() != null
    // ? airEmissionDataDTO.getAirAutoCalibrationInfo()
    // : "");
    // data.put("air_auto_incident_summary",
    // airEmissionDataDTO.getAirAutoIncidentSummary() != null
    // ? airEmissionDataDTO.getAirAutoIncidentSummary()
    // : "");
    // data.put("air_auto_downtime_desc",
    // airEmissionDataDTO.getAirAutoDowntimeDesc() != null
    // ? airEmissionDataDTO.getAirAutoDowntimeDesc()
    // : "");
    // data.put("air_auto_avg_calc_desc",
    // airEmissionDataDTO.getAirAutoAvgCalcDesc() != null
    // ? airEmissionDataDTO.getAirAutoAvgCalcDesc()
    // : "");
    // data.put("air_auto_avg_compare_desc",
    // airEmissionDataDTO.getAirAutoAvgCompareDesc() != null
    // ? airEmissionDataDTO.getAirAutoAvgCompareDesc()
    // : "");
    // data.put("air_auto_exceed_days_summary",
    // airEmissionDataDTO.getAirAutoExceedDaysSummary() != null
    // ? airEmissionDataDTO.getAirAutoExceedDaysSummary()
    // : "");
    // data.put("air_auto_abnormal_reason",
    // airEmissionDataDTO.getAirAutoAbnormalReason() != null
    // ? airEmissionDataDTO.getAirAutoAbnormalReason()
    // : "");
    // data.put("air_auto_completeness_review",
    // airEmissionDataDTO.getAirAutoCompletenessReview() != null
    // ? airEmissionDataDTO.getAirAutoCompletenessReview()
    // : "");
    // data.put("air_auto_exceed_conclusion",
    // airEmissionDataDTO.getAirAutoExceedConclusion() != null
    // ? airEmissionDataDTO.getAirAutoExceedConclusion()
    // : "");
    // }
    // if (wasteManagementDataDTO != null) {
    // data.put("sw_general_notes",
    // wasteManagementDataDTO.getSwGeneralNote() != null
    // ? wasteManagementDataDTO.getSwGeneralNote()
    // : "");
    // data.put("incident_plan_development",
    // wasteManagementDataDTO.getIncidentPlanDevelopment() != null
    // ? wasteManagementDataDTO.getIncidentPlanDevelopment()
    // : "");
    // data.put("incident_prevention_measures",
    // wasteManagementDataDTO.getIncidentPreventionMeasures() != null
    // ? wasteManagementDataDTO.getIncidentPreventionMeasures()
    // : "");
    // data.put("incident_response_report",
    // wasteManagementDataDTO.getIncidentResponseReport() != null
    // ? wasteManagementDataDTO.getIncidentResponseReport()
    // : "");
    // }
    // Resource resource = new
    // ClassPathResource("templates/reportA05/ReportA05_template.docx");
    // log.info("Loading template from: {}", resource.getFilename());
    // try (InputStream fis = resource.getInputStream();
    // XWPFDocument doc = new XWPFDocument(fis);
    // ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
    // // Thay thế trong paragraphs
    // for (XWPFParagraph paragraph : doc.getParagraphs()) {
    // replacePlaceholders(paragraph, data);
    // }
    // // Thay thế trong tables
    // for (XWPFTable table : doc.getTables()) {
    // for (XWPFTableRow row : table.getRows()) {
    // for (XWPFTableCell cell : row.getTableCells()) {
    // for (XWPFParagraph p : cell.getParagraphs()) {
    // replacePlaceholders(p, data);
    // }
    // }
    // }
    // }
    // if (wasteWaterDataDTO != null) {
    // // Bảng 1.1, 1.2 Thống Kê Vị Trí & Kết Quả Vượt Quy Chuẩn (QCVN) (nếu có)
    // if (wasteWaterDataDTO.getMonitoringExceedances() != null
    // && !wasteWaterDataDTO.getMonitoringExceedances().isEmpty()) {
    // log.info(" Filling Monitoring Exceedances table ({} records)",
    // wasteWaterDataDTO.getMonitoringExceedances().size());
    // TableMappingService.fillWasteWaterMonitoringTable(doc,
    // wasteWaterDataDTO.getMonitoringExceedances());
    // } else {
    // log.info("No Monitoring Exceedances data to fill.");
    // }
    // // bảng 1.3 Thống kê kết quả quan trắc tự động
    // if (wasteWaterDataDTO.getMonitoringStats() != null
    // && !wasteWaterDataDTO.getMonitoringStats().isEmpty()) {
    // log.info(" Filling Monitoring Stats table ({} records)",
    // wasteWaterDataDTO.getMonitoringStats().size());
    // TableMappingService.fillAutoMonitoringStatsTable(doc,
    // wasteWaterDataDTO.getMonitoringStats());
    // } else {
    // log.info("No Monitoring Stats data to fill.");
    // }
    // // bảng 1.4 Thống kê các sự cố của trạm
    // if (wasteWaterDataDTO.getMonitoringIncidents() != null
    // && !wasteWaterDataDTO.getMonitoringIncidents().isEmpty()) {
    // log.info(" Filling Monitoring Incidents table ({} records)",
    // wasteWaterDataDTO.getMonitoringIncidents().size());
    // TableMappingService.fillAutoMonitoringIncidentsTable(doc,
    // wasteWaterDataDTO.getMonitoringIncidents());
    // } else {
    // log.info("No Monitoring Incidents data to fill.");
    // }
    // // Bảng 1.5: Thống kê vượt QCVN (theo từng thông số)
    // if (wasteWaterDataDTO.getQcvnExceedances() != null
    // && !wasteWaterDataDTO.getQcvnExceedances().isEmpty()) {
    // log.info(" Filling QCVN Exceedances table ({} records)",
    // wasteWaterDataDTO.getQcvnExceedances().size());
    // TableMappingService.fillQcvnExceedancesTable(doc,
    // wasteWaterDataDTO.getQcvnExceedances());
    // } else {
    // log.info("No QCVN Exceedances data to fill.");
    // }
    // }
    // // Bảng 2
    // if (airEmissionDataDTO != null) {
    // // Bảng 2.1
    // if (airEmissionDataDTO.getAirMonitoringExceedances() != null
    // && !airEmissionDataDTO.getAirMonitoringExceedances().isEmpty()) {
    // log.info(" Filling Air Monitoring Exceedances table ({} records)",
    // airEmissionDataDTO.getAirMonitoringExceedances().size());
    // TableMappingService.fillAirMonitoringTable(doc,
    // airEmissionDataDTO.getAirMonitoringExceedances());
    // } else {
    // log.info("No Air Monitoring Exceedances data to fill.");
    // }
    // // 2.2
    // if (airEmissionDataDTO.getAirAutoMonitoringStats() != null
    // && !airEmissionDataDTO.getAirAutoMonitoringStats().isEmpty()) {
    // log.info(" Filling Air Auto Monitoring Stats table ({} records)",
    // airEmissionDataDTO.getAirAutoMonitoringStats().size());
    // TableMappingService.fillAirAutoMonitoringTable(doc,
    // airEmissionDataDTO.getAirAutoMonitoringStats());
    // } else {
    // log.info("No Air Auto Monitoring Stats data to fill.");
    // }
    // // 2.3
    // if (airEmissionDataDTO.getAirAutoMonitoringIncidents() != null
    // && !airEmissionDataDTO.getAirAutoMonitoringIncidents().isEmpty()) {
    // log.info(" Filling Air Auto Monitoring Incidents table ({} records)",
    // airEmissionDataDTO.getAirAutoMonitoringIncidents().size());
    // TableMappingService.fillAirAutoMonitoringIncidentsTable(doc,
    // airEmissionDataDTO.getAirAutoMonitoringIncidents());
    // } else {
    // log.info("No Air Auto Monitoring Incidents data to fill.");
    // }
    // // 2.4
    // if (airEmissionDataDTO.getAirAutoQcvnExceedances() != null
    // && !airEmissionDataDTO.getAirAutoQcvnExceedances().isEmpty()) {
    // log.info(" Filling Air QCVN Exceedances table ({} records)",
    // airEmissionDataDTO.getAirAutoQcvnExceedances().size());
    // TableMappingService.fillAirQcvnExceedancesTable(doc,
    // airEmissionDataDTO.getAirAutoQcvnExceedances());
    // } else {
    // log.info("No Air QCVN Exceedances data to fill.");
    // }
    // }
    // // Bảng 3,4
    // if (wasteManagementDataDTO != null) {
    // // 3.1
    // if (wasteManagementDataDTO.getDomesticSolidWasteStats() != null
    // && !wasteManagementDataDTO.getDomesticSolidWasteStats().isEmpty()) {
    // log.info(" Filling Domestic Solid Waste Stats table ({} records)",
    // wasteManagementDataDTO.getDomesticSolidWasteStats().size());
    // TableMappingService.fillDomesticSolidWasteStatsTable(doc,
    // wasteManagementDataDTO.getDomesticSolidWasteStats());
    // } else {
    // log.info("No Domestic Solid Waste Stats data to fill.");
    // }
    // // 3.2
    // if (wasteManagementDataDTO.getIndustrialSolidWasteStats() != null
    // && !wasteManagementDataDTO.getIndustrialSolidWasteStats().isEmpty()) {
    // log.info(" Filling Industrial Solid Waste Stats table ({} records)",
    // wasteManagementDataDTO.getIndustrialSolidWasteStats().size());
    // TableMappingService.fillIndustrialSolidWasteStatsTable(doc,
    // wasteManagementDataDTO.getIndustrialSolidWasteStats());
    // } else {
    // log.info("No Industrial Solid Waste Stats data to fill.");
    // }
    // // 3.3
    // if (wasteManagementDataDTO.getRecycleIndustrialWasteStats() != null
    // && !wasteManagementDataDTO.getRecycleIndustrialWasteStats().isEmpty()) {
    // log.info(" Filling Recycle Industrial Waste Stats table ({} records)",
    // wasteManagementDataDTO.getRecycleIndustrialWasteStats().size());
    // TableMappingService.fillRecycleIndustrialWasteTable(doc,
    // wasteManagementDataDTO.getRecycleIndustrialWasteStats());
    // } else {
    // log.info("No Recycle Industrial Waste Stats data to fill.");
    // }
    // // 3.4
    // if (wasteManagementDataDTO.getOtherSolidWasteStats() != null
    // && !wasteManagementDataDTO.getOtherSolidWasteStats().isEmpty()) {
    // log.info(" Filling Other Solid Waste Stats table ({} records)",
    // wasteManagementDataDTO.getOtherSolidWasteStats().size());
    // TableMappingService.fillOtherSolidWasteStatsTable(doc,
    // wasteManagementDataDTO.getOtherSolidWasteStats());
    // } else {
    // log.info("No Other Solid Waste Stats data to fill.");
    // }
    // // 4.1
    // if (wasteManagementDataDTO.getHazardousWasteStats() != null
    // && !wasteManagementDataDTO.getHazardousWasteStats().isEmpty()) {
    // log.info(" Filling Hazardous Waste Stats table ({} records)",
    // wasteManagementDataDTO.getHazardousWasteStats().size());
    // TableMappingService.fillHazardousWasteStatsTable(doc,
    // wasteManagementDataDTO.getHazardousWasteStats());
    // } else {
    // log.info("No Hazardous Waste Stats data to fill.");
    // }
    // // 4.2
    // if (wasteManagementDataDTO.getExportedHwStats() != null
    // && !wasteManagementDataDTO.getExportedHwStats().isEmpty()) {
    // log.info(" Filling Exported HW Stats table ({} records)",
    // wasteManagementDataDTO.getExportedHwStats().size());
    // TableMappingService.fillExportedHwStatsTable(doc,
    // wasteManagementDataDTO.getExportedHwStats());
    // } else {
    // log.info("No Exported HW Stats data to fill.");
    // }
    // // 4.3
    // if (wasteManagementDataDTO.getSelfTreatedHwStats() != null
    // && !wasteManagementDataDTO.getSelfTreatedHwStats().isEmpty()) {
    // log.info(" Filling Self Treated HW Stats table ({} records)",
    // wasteManagementDataDTO.getSelfTreatedHwStats().size());
    // TableMappingService.fillSeftTreatedHwStatsTable(doc,
    // wasteManagementDataDTO.getSelfTreatedHwStats());
    // } else {
    // log.info("No Self Treated HW Stats data to fill.");
    // }
    // // 7.1
    // if (wasteManagementDataDTO.getPopInventoryStats() != null
    // && !wasteManagementDataDTO.getPopInventoryStats().isEmpty()) {
    // log.info(" Filling POP Inventory Stats table ({} records)",
    // wasteManagementDataDTO.getPopInventoryStats().size());
    // TableMappingService.fillPopInventoryStatsTable(doc,
    // wasteManagementDataDTO.getPopInventoryStats());
    // } else {
    // log.info("No POP Inventory Stats data to fill.");
    // }
    // }
    // doc.write(baos);
    // byte[] result = baos.toByteArray();
    // // Ghi ra file để kiểm tra (optional)
    // // String outputDir = "D:\\Cao Ha\\eipFolder\\generated\\reports";
    // // Files.createDirectories(Paths.get(outputDir));
    // // String fileName = String.format("%s/ReportA05_%s_%s.docx",
    // // outputDir,
    // // business.getFacilityName().replaceAll("[^a-zA-Z0-9]", "_"),
    // // reportId);
    // // Files.write(Paths.get(fileName), result);
    // // log.info(" File generated: {}", fileName);
    // ReportA05DTO reportDTO = ReportA05DTO.builder()
    // .reportYear(report.getReportYear())
    // .build();
    // String savedFilePath = saveReportFile(result, reportId, business, reportDTO);
    // log.info("✅ Report file generated and saved: {} ({} bytes)", savedFilePath,
    // result.length);
    // return result;
    // }
    // }
    // private String saveReportFile(byte[] fileBytes, UUID reportId, BusinessDetail
    // business, ReportA05DTO report) {
    // try {
    // // Tạo subfolder theo năm: reporta05/2025/
    // Integer reportYear = report.getReportYear() != null ? report.getReportYear()
    // : LocalDateTime.now().getYear();
    // Path reportDir = Paths.get(uploadDir, "reporta05",
    // String.valueOf(reportYear));
    // // Tạo folder nếu chưa có
    // Files.createDirectories(reportDir);
    // log.info("📁 Report directory: {}", reportDir);
    // // Tạo tên file
    // String timestamp =
    // LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    // String facilityName = business.getFacilityName() != null
    // ? sanitizeFileName(business.getFacilityName())
    // : "Unknown";
    // String fileName = String.format("BaoCaoA05_%s_%s_%s.docx",
    // facilityName,
    // reportId.toString().substring(0, 8),
    // timestamp);
    // // Lưu file
    // Path filePath = reportDir.resolve(fileName);
    // Files.write(filePath, fileBytes);
    // // Return relative path
    // String relativePath = String.format("reporta05/%d/%s", reportYear, fileName);
    // log.info(" File saved successfully: {}", relativePath);
    // return relativePath;
    // } catch (IOException e) {
    // log.error("⚠️ Could not save report file: {}", e.getMessage(), e);
    // throw new RuntimeException("Failed to save report file", e);
    // }
    // }
    // private String sanitizeFileName(String input) {
    // if (input == null || input.isEmpty()) {
    // return "Unknown";
    // }
    // String sanitized = input
    // .replaceAll("[/\\\\:*?\"<>|]", "") // Loại bỏ ký tự không hợp lệ
    // .replaceAll("\\s+", "_") // Thay space = underscore
    // .trim();
    // // Giới hạn độ dài
    // if (sanitized.length() > 50) {
    // sanitized = sanitized.substring(0, 50);
    // }
    // return sanitized;
    // }
    /**
     * Format LocalDate to dd/MM/yyyy or return empty if null
     */
    // private String formatDate(LocalDate date) {
    // if (date == null) {
    // return "";
    // }
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // return date.format(formatter);
    // }
    // /**
    // * PHƯƠNG THỨC ĐÃ SỬA - Xử lý placeholder bị tách thành nhiều runs
    // */
    // private void replacePlaceholders(XWPFParagraph paragraph, Map<String, String>
    // data) {
    // // Lấy toàn bộ text của paragraph
    // String fullText = paragraph.getText();
    // if (fullText == null || fullText.isEmpty()) {
    // return;
    // }
    // // Thay thế tất cả placeholders
    // boolean modified = false;
    // for (Map.Entry<String, String> entry : data.entrySet()) {
    // String placeholder = "{{" + entry.getKey() + "}}";
    // if (fullText.contains(placeholder)) {
    // String value = entry.getValue();
    // if (value == null) {
    // value = ""; // Thay thế null bằng empty string
    // }
    // fullText = fullText.replace(placeholder, value);
    // modified = true;
    // }
    // }
    // // Nếu có thay đổi, xóa hết runs cũ và tạo run mới
    // if (modified) {
    // // Lưu formatting của run đầu tiên (nếu có)
    // XWPFRun firstRun = paragraph.getRuns().isEmpty() ? null :
    // paragraph.getRuns().get(0);
    // // Xóa tất cả runs cũ
    // int runCount = paragraph.getRuns().size();
    // for (int i = runCount - 1; i >= 0; i--) {
    // paragraph.removeRun(i);
    // }
    // // Tạo run mới với text đã thay thế
    // XWPFRun newRun = paragraph.createRun();
    // newRun.setText(fullText);
    // // Copy formatting từ run cũ nếu có
    // if (firstRun != null) {
    // copyRunFormatting(firstRun, newRun);
    // }
    // }
    // }
    /**
     * PHƯƠNG THỨC MỚI - Copy formatting từ run cũ sang run mới
     */
    // private void copyRunFormatting(XWPFRun source, XWPFRun target) {
    // try {
    // if (source.getFontFamily() != null) {
    // target.setFontFamily(source.getFontFamily());
    // }
    // if (source.getFontSize() != -1) {
    // target.setFontSize(source.getFontSize());
    // }
    // target.setBold(source.isBold());
    // target.setItalic(source.isItalic());
    // target.setUnderline(source.getUnderline());
    // if (source.getColor() != null) {
    // target.setColor(source.getColor());
    // }
    // } catch (Exception e) {
    // log.warn("Could not copy all formatting: {}", e.getMessage());
    // }
    // }
}
