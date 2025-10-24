package com.EIPplatform.repository.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.userInformation.UserHistoryConsumption;

@Repository
public interface UserHistoryConsumptionRepository extends JpaRepository<UserHistoryConsumption, Long> {
    Optional<UserHistoryConsumption> findByUserHistoryConsumptionId(Long id);
    void deleteByUserHistoryConsumptionId(Long id);
    List<UserHistoryConsumption> findByUserDetailUserDetailId(UUID userDetailId);
}
