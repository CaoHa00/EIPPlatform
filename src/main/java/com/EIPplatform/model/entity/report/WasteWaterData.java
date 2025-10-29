package com.EIPplatform.model.entity.report;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "waste_water_data", indexes = {
    @Index(name = "idx_waste_water_report_id", columnList = "report_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteWaterData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ww_id", updatable = false, nullable = false)
    Long wwId;
    
    // ============= QUAN HỆ ONE-TO-ONE VỚI REPORT_A05 =============
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    @JsonBackReference(value = "report-wastewater")
    Report_A05 report;
    
    // ============= NƯỚC THẢI SINH HOẠT =============
    @Column(name = "treatment_ww_desc", columnDefinition = "NVARCHAR(MAX)")
    String treatmentWwDesc; // Mô tả hệ thống xử lý nước thải
    
    @Column(name = "dom_ww_cy", nullable = false)
    Double domWwCy; // Lượng nước thải sinh hoạt phát sinh - năm báo cáo (m3)
    
    @Column(name = "dom_ww_py", nullable = false)
    Double domWwPy; // Lượng nước thải sinh hoạt phát sinh - năm trước (m3)
    
    @Column(name = "dom_ww_design", nullable = false)
    Double domWwDesign; // Tổng lưu lượng nước thải sinh hoạt theo thiết kế (m3)
    
    // ============= NƯỚC THẢI CÔNG NGHIỆP =============
    @Column(name = "industrial_ww_cy", nullable = false)
    Double industrialWwCy; // Lượng nước thải công nghiệp - năm báo cáo (m3)
    
    @Column(name = "industrial_ww_py", nullable = false)
    Double industrialWwPy; // Lượng nước thải công nghiệp - năm trước (m3)
    
    @Column(name = "industrial_ww_design", nullable = false)
    Double industrialWwDesign; // Tổng lưu lượng nước thải công nghiệp theo thiết kế (m3)
    
    // ============= NƯỚC LÀM MÁT =============
    @Column(name = "cooling_water_cy")
    Double coolingWaterCy; // Lượng nước làm mát - năm báo cáo (m3)
    
    @Column(name = "cooling_water_py")
    Double coolingWaterPy; // Lượng nước làm mát - năm trước (m3)
    
    @Column(name = "cooling_water_design")
    Double coolingWaterDesign; // Tổng lưu lượng nước làm mát theo thiết kế (m3)
    
    // ============= TÌNH HÌNH ĐẤU NỐI HỆ THỐNG XLNT TẬP TRUNG =============
    @Column(name = "connection_status_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String connectionStatusDesc; // Mô tả tình hình đấu nối
    
    @Column(name = "connection_diagram")
    String connectionDiagram; // Đường dẫn file sơ đồ đấu nối (URL hoặc path)
}
