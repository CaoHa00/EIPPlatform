package com.EIPplatform.repository.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.userInformation.UserDetail;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, UUID> {

    Optional<UserDetail> findByUserAccount(UserAccount userAccount);

    Optional<UserDetail> findByUserAccount_UserAccountId(UUID userAccountId);

    boolean existsByTaxCode(String taxCode);

    Optional<UserDetail> findByTaxCode(String taxCode);

    @Query("SELECT ud FROM UserDetail ud WHERE " +
            "LOWER(ud.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(ud.taxCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<UserDetail> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT ud.industrySector, COUNT(ud) FROM UserDetail ud " +
            "WHERE ud.industrySector IS NOT NULL " +
            "GROUP BY ud.industrySector")
    List<Object[]> countByIndustrySector();

    boolean existsByBusinessLicenseNo(String businessLicenseNo);

    Optional<UserDetail> findByBusinessLicenseNo(String businessLicenseNo);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserDetail> findByPhoneNumber(String phoneNumber);
}