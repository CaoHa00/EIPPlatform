package com.EIPplatform.repository.authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.authentication.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByUserAccountId(UUID id);

    void deleteByUserAccountId(UUID id);

    List<UserAccount> findByUserDetailUserDetailId(UUID userDetailId);

    @Query("""
            select u
            from UserAccount u
            where u.email = :email
            """)
    Optional<UserAccount> findUserByEmail(@Param("email") String email);

    @Query("""
            select count(u) > 0
            from UserAccount u
            where u.email = :email
            """)
    boolean existsByEmail(String email);

    @Query("""
                SELECT u
                FROM UserAccount u
                LEFT JOIN FETCH u.roles
                WHERE u.email = :email
            """)
    Optional<UserAccount> findUserWithRolesByEmail(@Param("email") String email);

    Optional<UserAccount> findByEmail(String email);
}
