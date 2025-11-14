//package com.EIPplatform.model.entity.report.report06.part03;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.UuidGenerator;
//import java.lang.Double;
//import java.util.UUID;
//
///**
// * Bảng result: Chi tiết Mục 3 - Kết quả kiểm kê (các trường 3.1-3.4), và Mục 4.1-4.3 (thuyết minh độ tin cậy, đầy đủ, không chắc chắn).
// * OneToOne với InventoryResultData để tránh bloat entity chính.
// */
//@Entity
//@Table(name = "result")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class Result {
//    @Id
//    @GeneratedValue
//    @UuidGenerator
//    @Column(name = "result_id", updatable = false, nullable = false)
//    UUID resultId; // ID chính của bảng result
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "inventory_result_data_id", nullable = false, unique = true)
//    @JsonBackReference(value = "inv-result-result")
//    InventoryResultData inventoryResultData; // Bidirectional backref: Liên kết với InventoryResultData (bảng inventory_result_data)
//
//    // ============================================================
//    // MỤC 3: Kết quả kiểm kê
//    // ============================================================
//    @Column(name = "production_unit_name", columnDefinition = "NVARCHAR(255)") // Mục 3.1: production_unit_name - Tên đơn vị tính cường độ phát thải (VARCHAR(255), optional)
//            String productionUnitName;
//
//    @Column(name = "total_production_output", precision = 14) // Mục 3.2: total_production_output - Tổng sản lượng / Giá trị (DECIMAL(14,2), optional nhưng bắt buộc nếu 3.1 có)
//    Double totalProductionOutput;
//
//    @Column(name = "intensity_ratio_result", scale = 4) // Mục 3.3: intensity_ratio_result - Cường độ phát thải (DECIMAL(10,4), auto-calculated, read-only)
//    Double intensityRatioResult;
//
//    @Column(name = "result_commentary", columnDefinition = "NVARCHAR(MAX)") // Mục 3.4: result_commentary - Thuyết minh / Nhận xét kết quả (TEXT, optional)
//    String resultCommentary;
//
//    // ============================================================
//    // MỤC 4: Độ tin cậy, tính đầy đủ, độ không chắc chắn (4.1-4.3)
//    // ============================================================
//    @Column(name = "reliability_statement", columnDefinition = "NVARCHAR(MAX)") // Mục 4.1: reliability_statement - Thuyết minh về Độ tin cậy (TEXT, optional)
//            String reliabilityStatement;
//
//    @Column(name = "completeness_statement", columnDefinition = "NVARCHAR(MAX)") // Mục 4.2: completeness_statement - Thuyết minh về Tính đầy đủ (TEXT, optional)
//    String completenessStatement;
//
//    @Column(name = "uncertainty_methodology", columnDefinition = "NVARCHAR(MAX)") // Mục 4.3: uncertainty_methodology - Phương pháp luận đánh giá độ không chắc chắn (TEXT, optional)
//    String uncertaintyMethodology;
//}