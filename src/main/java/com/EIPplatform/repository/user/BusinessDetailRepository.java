package com.EIPplatform.repository.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.user.businessInformation.BusinessHistoryConsumption;

@Repository
public interface BusinessDetailRepository extends JpaRepository<BusinessDetail, UUID> {

    Optional<BusinessDetail> findByUserAccountsContaining(UserAccount userAccount);

    Optional<BusinessDetail> findByUserAccounts_UserAccountId(UUID userAccountId); // service

    boolean existsByTaxCode(String taxCode);

    Optional<BusinessDetail> findByTaxCode(String taxCode);

    @Query("SELECT ud FROM BusinessDetail ud WHERE "
            + "LOWER(ud.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(ud.taxCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BusinessDetail> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT ud.industrySector, COUNT(ud) FROM BusinessDetail ud "
            + "WHERE ud.industrySector IS NOT NULL "
            + "GROUP BY ud.industrySector")
    List<Object[]> countByIndustrySector();

    boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber); //service

    Optional<BusinessDetail> findByBusinessRegistrationNumber(String businessRegistrationNumber); // service

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<BusinessDetail> findByPhoneNumber(String phoneNumber);
}