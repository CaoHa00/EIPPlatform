package com.EIPplatform.repository.authentication;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.EIPplatform.model.entity.user.authentication.PasswordChangeCode;

public interface PasswordChangeCodeRepository extends JpaRepository<PasswordChangeCode, UUID> {

    @Query(value = """
            select pcc.*
            from password_change_code pcc
            where pcc.email = :email
            and pcc.code = :code
            and pcc.used = 0
            """, nativeQuery = true)
    Optional<PasswordChangeCode> findUnusedVerificationCode(@Param("email") String email, @Param("code") String code);

    @Modifying
    @Query(value = """
            delete from password_change_code where expires_at <= :now
            """, nativeQuery = true)
    void deleteByExpiresAtBefore(@Param("now") LocalDateTime dateTime);

    @Query("SELECT COUNT(p) FROM PasswordChangeCode p WHERE p.email = :email AND p.createdAt >= :since")
    Long countRecentCodesByEmail(@Param("email") String email, @Param("since") LocalDateTime since);
}
