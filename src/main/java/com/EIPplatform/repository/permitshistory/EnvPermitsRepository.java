package com.EIPplatform.repository.permitshistory;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.permitshistory.EnvPermits;

@Repository
public interface EnvPermitsRepository extends JpaRepository<EnvPermits, Long> {

    // Basic query methods for Main Permit (1:1 relationship)
    Optional<EnvPermits> findByBusinessDetail_BusinessDetailId(UUID businessDetailId);

    boolean existsByBusinessDetail_BusinessDetailId(UUID businessDetailId);

    // Query using userAccountId
    @Query("SELECT p FROM EnvPermits p " +
            "JOIN p.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId")
    Optional<EnvPermits> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM EnvPermits p " +
            "JOIN p.businessDetail bd " +
            "JOIN bd.userAccounts ua " +
            "WHERE ua.userAccountId = :userId")
    boolean existsByUserId(@Param("userId") UUID userId);

}