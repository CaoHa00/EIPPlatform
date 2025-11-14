package com.EIPplatform.repository.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.legalDoc.LegalDoc;

@Repository
public interface LegalDocRepository extends JpaRepository<LegalDoc, UUID> {
    
    List<LegalDoc> findByInvestorOrganization_InvestorId(UUID investorOrganizationId);
    
    boolean existsByNumberAndInvestorOrganization_InvestorId(String number, UUID investorOrganizationId);
    
    boolean existsByNumberAndInvestorOrganization_InvestorIdAndLegalDocIdNot(String number, UUID investorOrganizationId, UUID legalDocId);
}