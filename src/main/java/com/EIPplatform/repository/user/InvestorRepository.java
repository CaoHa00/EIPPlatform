package com.EIPplatform.repository.user;

import com.EIPplatform.model.entity.user.investors.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, UUID>, JpaSpecificationExecutor<Investor> {
    
    /**
     * Kiểm tra taxCode trên bảng Investor cha (áp dụng cho cả Individual và Organization)
     * Rất hữu ích để đảm bảo taxCode là duy nhất trên toàn hệ thống.
     */
    boolean existsByTaxCode(String taxCode);

    /**
     * Tương tự, dùng để kiểm tra khi cập nhật.
     */
    boolean existsByTaxCodeAndInvestorIdNot(String taxCode, UUID investorId);
}