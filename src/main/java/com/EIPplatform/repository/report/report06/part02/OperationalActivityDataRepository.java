package com.EIPplatform.repository.report.report06.part02;

import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OperationalActivityDataRepository extends JpaRepository<OperationalActivityData, UUID> {
}
