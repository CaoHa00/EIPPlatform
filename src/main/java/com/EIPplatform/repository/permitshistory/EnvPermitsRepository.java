package com.EIPplatform.repository.permitshistory;

import com.EIPplatform.model.entity.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EnvPermitsRepository extends JpaRepository<EnvPermits, Long> {

    List<EnvPermits> findByBusinessDetailAndIsActive(
            BusinessDetail businessDetail, Boolean isActive
    );

    List<EnvPermits> findByBusinessDetail(BusinessDetail businessDetail);

    @Query("SELECT ep FROM EnvPermits ep WHERE ep.businessDetail.businessDetailId = :userId " +
            "AND (:permitType IS NULL OR ep.permitType = :permitType) " +
            "AND (:isActive IS NULL OR ep.isActive = :isActive) " +
            "AND (:issueDateFrom IS NULL OR ep.issueDate >= :issueDateFrom) " +
            "AND (:issueDateTo IS NULL OR ep.issueDate <= :issueDateTo)")
    List<EnvPermits> findByUserAndFilters(
            @Param("userId") UUID userId,
            @Param("permitType") String permitType,
            @Param("isActive") Boolean isActive,
            @Param("issueDateFrom") LocalDate issueDateFrom,
            @Param("issueDateTo") LocalDate issueDateTo
    );

    @Query("SELECT ep FROM EnvPermits ep WHERE ep.businessDetail.businessDetailId = :userId " +
            "AND ep.issueDate < :expiryThreshold " +
            "AND ep.isActive = true")
    List<EnvPermits> findExpiringPermits(
            @Param("userId") UUID userId,
            @Param("expiryThreshold") LocalDate expiryThreshold
    );

    List<EnvPermits> findByBusinessDetail_BusinessDetailId(UUID businessDetailId);

    @Query("SELECT COUNT(ep) FROM EnvPermits ep " +
            "WHERE ep.businessDetail.businessDetailId = :userId AND ep.isActive = true")
    Long countActivePermits(@Param("userId") UUID userId);
}