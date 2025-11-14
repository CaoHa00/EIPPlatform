package com.EIPplatform.repository.report.report06.part02;

import com.EIPplatform.model.entity.report.report06.part02.CarbonSinkProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarbonSinkProjectRepository extends JpaRepository<CarbonSinkProject, UUID> {
    List<CarbonSinkProject> findByOperationalActivityData_OperationalActivityDataId(UUID id);
}
