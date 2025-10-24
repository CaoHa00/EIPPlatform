package com.EIPplatform.repository.user;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.userInformation.UserDetail;
@Repository
public interface  UserDetailRepository extends JpaRepository<UserDetail, UUID> {
    Optional<UserDetail> findByUserDetailId(UUID id);
    void deleteByUserDetailId(UUID id);
}
