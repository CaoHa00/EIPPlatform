package com.EIPplatform.repository.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.entity.permitshistory.EnvComponentPermit;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.user.businessInformation.BusinessHistoryConsumption;

@Repository
public interface BusinessDetailRepository extends JpaRepository<BusinessDetail, UUID> {

    @Query("SELECT bd FROM BusinessDetail bd JOIN bd.userAccounts ua WHERE ua.userAccountId = :userAccountId")
    Optional<BusinessDetail> findByUserAccountId(@Param("userAccountId") UUID userAccountId);

    Optional<BusinessDetail> findByUserAccountsContaining(UserAccount userAccount);

    Optional<BusinessDetail> findByUserAccounts_UserAccountId(UUID userAccountId); // service

    boolean existsByTaxCode(String taxCode);

    Optional<BusinessDetail> findByTaxCode(String taxCode);

    @Query("SELECT ud FROM BusinessDetail ud WHERE "
            + "LOWER(ud.facilityName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(ud.taxCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BusinessDetail> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT ud.activityType, COUNT(ud) FROM BusinessDetail ud "
            + "WHERE ud.activityType IS NOT NULL "
            + "GROUP BY ud.activityType")
    List<Object[]> countByIndustrySector();

    boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber); //service

    Optional<BusinessDetail> findByBusinessRegistrationNumber(String businessRegistrationNumber); // service

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<BusinessDetail> findByPhoneNumber(String phoneNumber);

    @Query("SELECT e FROM EnvComponentPermit e WHERE e.businessDetail.businessDetailId = :businessDetailId")
    List<EnvComponentPermit> findByBusinessDetailBusinessDetailId(UUID businessDetailId);

    @Modifying  // Để JPA biết đây là update/delete query
    @Query("DELETE FROM EnvComponentPermit e WHERE e.businessDetail.businessDetailId = :businessDetailId")
    void deleteByBusinessDetailBusinessDetailId(@Param("businessDetailId") UUID businessDetailId);
}