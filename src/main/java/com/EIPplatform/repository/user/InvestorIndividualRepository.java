package com.EIPplatform.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.investors.InvestorIndividualDetail;

import java.util.UUID;

@Repository
public interface InvestorIndividualRepository extends JpaRepository<InvestorIndividualDetail, UUID> {
    boolean existsByTaxCode(String taxCode);
    boolean existsByTaxCodeAndInvestorIdNot(String taxCode, UUID investorId);
}