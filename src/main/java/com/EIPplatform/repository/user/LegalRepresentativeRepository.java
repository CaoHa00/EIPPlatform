package com.EIPplatform.repository.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.businessInformation.legalRepresentative.LegalRepresentative;

@Repository
public interface LegalRepresentativeRepository extends JpaRepository<LegalRepresentative, UUID> {
    boolean existsByTaxCode(String taxCode);

    Optional<LegalRepresentative> findByTaxCode(String taxCode);

    boolean existsByTaxCodeAndLegalRepresentativeIdNot(String taxCode, UUID legalRepresentativeId);
}
