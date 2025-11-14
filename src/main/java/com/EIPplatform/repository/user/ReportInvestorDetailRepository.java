package com.EIPplatform.repository.user;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportInvestorDetailRepository extends JpaRepository<ReportInvestorDetail, Long>, JpaSpecificationExecutor<ReportInvestorDetail> {
    // JpaRepository đã cung cấp đủ các hàm (findById, save, findAll, deleteById)
    // mà Service của bạn cần.
    // Bạn có thể thêm các hàm tìm kiếm tùy chỉnh ở đây nếu cần.
}