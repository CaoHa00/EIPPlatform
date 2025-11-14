package com.EIPplatform.repository.report.reportB04.part1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.report.reportB04.part01.ThirdPartyImplementer;
@Repository
public interface ThirdPartyImplementerRepository extends JpaRepository<ThirdPartyImplementer, Long> {
}