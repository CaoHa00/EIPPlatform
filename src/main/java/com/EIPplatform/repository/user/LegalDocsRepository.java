package com.EIPplatform.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.legalDocs.LegalDocs;

import java.util.List;
import java.util.UUID;

@Repository
public interface LegalDocsRepository extends JpaRepository<LegalDocs, UUID> {
    
    List<LegalDocs> findByInvestorOrganization_InvestorId(UUID investorOrganizationId);
    
    boolean existsByNumberAndInvestorOrganization_InvestorId(String number, UUID investorOrganizationId);
    
    boolean existsByNumberAndInvestorOrganization_InvestorIdAndLegalDocIdNot(String number, UUID investorOrganizationId, UUID legalDocId);
}