package com.EIPplatform.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.investors.InvestorOrganizationDetail;

import java.util.UUID;

@Repository
public interface InvestorOrganizationRepository extends JpaRepository<InvestorOrganizationDetail, UUID> {
    boolean existsByTaxCode(String taxCode);
    boolean existsByTaxCodeAndInvestorIdNot(String taxCode, UUID investorId);
}