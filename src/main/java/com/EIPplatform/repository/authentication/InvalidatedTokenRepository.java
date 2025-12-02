package com.EIPplatform.repository.authentication;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.model.entity.user.authentication.InvalidatedToken;


public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

    @Query("""
            select it
            from InvalidatedToken it
            where it.id = :id
            """)
    Optional<InvalidatedToken> findById(@Param("id") String id);

    @Query("""
                       select count(it) > 0
            from InvalidatedToken it
                       where it.id = :id
                       """)
    boolean existsById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM InvalidatedToken t WHERE t.issueTime < :now")
    void deleteExpiredTokens(@Param("now") Date now);
}
