package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.investors.*;
import com.EIPplatform.model.entity.businessInformation.investors.Investor;
import com.EIPplatform.model.entity.businessInformation.investors.InvestorIndividualDetail;
import com.EIPplatform.model.entity.businessInformation.investors.InvestorOrganizationDetail;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    // Dùng LegalDocMapper để map List<LegalDoc> -> List<LegalDocResponse>
    uses = { LegalDocMapper.class },
    // Bỏ qua các trường không map được (như investorType từ entity -> DTO)
    // chúng ta sẽ map thủ công
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface InvestorMapper {

    // =================================================================
    // 1. DTO (CreateRequest) -> ENTITY
    // =================================================================

    /**
     * Map DTO tạo mới Cá nhân sang Entity.
     * Các trường chung (address, phone...) và riêng (name, dateOfBirth...)
     * sẽ tự động được map.
     */
    @Mapping(target = "investorId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    InvestorIndividualDetail toEntity(InvestorIndividualCreationRequest request);

    /**
     * Map DTO tạo mới Tổ chức sang Entity.
     */
    @Mapping(target = "investorId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "legalDocs", ignore = true) // DTO create không có list này
    InvestorOrganizationDetail toEntity(InvestorOrganizationCreationRequest request);

    // =================================================================
    // 2. DTO (UpdateRequest) -> ENTITY (Sử dụng @MappingTarget)
    // =================================================================

    /**
     * Cập nhật Entity Cá nhân từ DTO Update.
     * Chỉ cập nhật các trường không-null từ DTO.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "investorId", ignore = true) // Không update ID
    @Mapping(target = "auditMetaData", ignore = true) // Không update audit
    void updateEntity(InvestorIndividualUpdateRequest request, @MappingTarget InvestorIndividualDetail entity);

    /**
     * Cập nhật Entity Tổ chức từ DTO Update.
     * * !!! QUAN TRỌNG:
     * Trường 'legalDocs' (kiểu String trong DTO) bị IGNORE.
     * MapStruct không thể tự map String -> List<LegalDoc>.
     * Bạn *bắt buộc* phải xử lý logic này thủ công trong Service.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "investorId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "legalDocs", ignore = true) // BẮT BUỘC IGNORE
    void updateEntity(InvestorOrganizationUpdateRequest request, @MappingTarget InvestorOrganizationDetail entity);

    // =================================================================
    // 3. ENTITY -> DTO (Concrete Response)
    // =================================================================

    /**
     * Map Entity Cá nhân sang DTO Response Cá nhân.
     * Chúng ta gán cứng 'investorType' vì DTO có trường này
     * nhưng Entity thì không (nó là DiscriminatorValue).
     */
    @Mapping(target = "investorType", constant = "INDIVIDUAL")
    InvestorIndividualResponse toIndividualResponse(InvestorIndividualDetail individual);

    /**
     * Map Entity Tổ chức sang DTO Response Tổ chức.
     * 'legalDocs' sẽ được map tự động bằng LegalDocMapper.
     */
    @Mapping(target = "investorType", constant = "ORGANIZATION")
    InvestorOrganizationResponse toOrganizationResponse(InvestorOrganizationDetail organization);

    // =================================================================
    // 4. POLYMORPHIC DISPATCHER (Entity -> Base Response)
    // =================================================================

    /**
     * Phương thức "điều phối" đa hình.
     * Nó sẽ được ReportInvestorDetailMapper gọi.
     * Tên (Named) là "toInvestorResponse" để khớp với DTO base của bạn
     * (InvestorIndividualResponse extends InvestorResponse).
     */
    @Named("toInvestorResponse") 
    default InvestorResponse toResponse(Investor investor) {
        if (investor == null) {
            return null;
        }
        
        // Kiểm tra kiểu cụ thể và gọi mapper tương ứng
        if (investor instanceof InvestorIndividualDetail ind) {
            return toIndividualResponse(ind); // Trả về InvestorIndividualResponse
        } else if (investor instanceof InvestorOrganizationDetail org) {
            return toOrganizationResponse(org); // Trả về InvestorOrganizationResponse
        }
        
        // Ném lỗi nếu gặp kiểu Investor không xác định
        throw new IllegalArgumentException("Unknown investor type: " + investor.getClass().getName());
    }
}