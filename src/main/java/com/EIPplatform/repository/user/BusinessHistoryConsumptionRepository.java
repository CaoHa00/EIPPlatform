package com.EIPplatform.repository.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.user.businessInformation.BusinessHistoryConsumption;

@Repository
public interface BusinessHistoryConsumptionRepository extends JpaRepository<BusinessHistoryConsumption, Long> {
    Optional<BusinessHistoryConsumption> findByBusinessHistoryConsumptionId(Long id);
    void deleteByBusinessDetail_BusinessDetailId(UUID id);
    List<BusinessHistoryConsumption> findByBusinessDetail_BusinessDetailId(UUID businessDetailId);
}
