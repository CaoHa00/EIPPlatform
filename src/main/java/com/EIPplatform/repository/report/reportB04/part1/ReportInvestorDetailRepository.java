package com.EIPplatform.repository.report.reportB04.part1;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportInvestorDetailRepository extends JpaRepository<ReportInvestorDetail, Long>, JpaSpecificationExecutor<ReportInvestorDetail> {

}