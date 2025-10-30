package com.EIPplatform.repository.permitshistory;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.permitshistory.EnvComponentPermit;

@Repository
public interface EnvComponentPermitRepository extends JpaRepository<EnvComponentPermit, Long> {

    // Basic query methods by businessDetailId
    List<EnvComponentPermit> findByBusinessDetail_BusinessDetailId(UUID businessDetailId);

    List<EnvComponentPermit> findByBusinessDetail_BusinessDetailIdAndPermitType(
            UUID businessDetailId, String permitType);

    List<EnvComponentPermit> findByBusinessDetail_BusinessDetailIdAndIsActive(
            UUID businessDetailId, Boolean isActive);

    boolean existsByBusinessDetail_BusinessDetailIdAndPermitNumber(
            UUID businessDetailId, String permitNumber);

    long countByBusinessDetail_BusinessDetailId(UUID businessDetailId);

    long countByBusinessDetail_BusinessDetailIdAndIsActive(UUID businessDetailId, Boolean isActive);

    // Query using userAccountId
    @Query("SELECT cp FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId " +
            "ORDER BY cp.createdAt DESC")
    List<EnvComponentPermit> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT cp FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId " +
            "AND cp.isActive = :isActive " +
            "ORDER BY cp.createdAt DESC")
    List<EnvComponentPermit> findByUserIdAndIsActive(
            @Param("userId") UUID userId,
            @Param("isActive") Boolean isActive);

    @Query("SELECT cp FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId " +
            "AND cp.permitType = :permitType " +
            "ORDER BY cp.createdAt DESC")
    List<EnvComponentPermit> findByUserIdAndPermitType(
            @Param("userId") UUID userId,
            @Param("permitType") String permitType);

    // Search by keyword
    @Query("SELECT cp FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId " +
            "AND (LOWER(cp.permitType) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(cp.permitNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(cp.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(cp.issuerOrg) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY cp.createdAt DESC")
    List<EnvComponentPermit> searchPermitsByKeyword(
            @Param("userId") UUID userId,
            @Param("keyword") String keyword);

    // Statistics
    @Query("SELECT cp.permitType, COUNT(cp) FROM EnvComponentPermit cp " +
            "WHERE cp.businessDetail.businessDetailId = :businessDetailId " +
            "GROUP BY cp.permitType")
    List<Object[]> countPermitsByType(@Param("businessDetailId") UUID businessDetailId);

    @Query("SELECT COUNT(cp) FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId")
    long countByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(cp) FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId " +
            "AND cp.isActive = true")
    long countActiveByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(cp) FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId " +
            "AND cp.isActive = false")
    long countInactiveByUserId(@Param("userId") UUID userId);

    // File-related queries
    @Query("SELECT cp FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId " +
            "AND cp.permitFilePath IS NOT NULL")
    List<EnvComponentPermit> findPermitsWithFile(@Param("userId") UUID userId);

    @Query("SELECT cp FROM EnvComponentPermit cp " +
            "JOIN cp.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId " +
            "AND cp.permitFilePath IS NULL")
    List<EnvComponentPermit> findPermitsWithoutFile(@Param("userId") UUID userId);
}