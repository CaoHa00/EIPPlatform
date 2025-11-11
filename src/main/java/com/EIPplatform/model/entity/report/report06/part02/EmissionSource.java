//package com.EIPplatform.model.entity.report.report06.part02;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.UuidGenerator;
//import java.math.BigDecimal;
//import java.util.UUID;
//
///**
// * EmissionSource: Nguồn phát thải KNK (Mục 3 Phần II - Bảng 3.1: Danh mục chi tiết nguồn phát thải KNK).
// * Lưu chi tiết từng nguồn phát thải, hỗ trợ multiple sources per report.
// */
//@Entity
//@Table(name = "emission_source")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class EmissionSource {
//    @Id
//    @GeneratedValue
//    @UuidGenerator
//    @Column(name = "emission_source_id", updatable = false, nullable = false)
//    UUID emissionSourceId; // ID chính (PK) của bảng emission_source
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "operational_activity_data_id", nullable = false)
//    @JsonBackReference(value = "op-data-emission-sources")
//    OperationalActivityData operationalActivityData; // Liên kết ngược với OperationalActivityData (bảng operational_activity_data, Phần II chính)
//
//    @Column(name = "source_scope", nullable = false)
//    Integer sourceScope; // Mục 3, Bảng 3.1: Phạm vi phát thải (1: Trực tiếp, 2: Gián tiếp năng lượng, 3: Gián tiếp khác) (not_null, Integer 1-3)
//
//    @Column(name = "source_category", nullable = false, columnDefinition = "NVARCHAR(255)")
//    String sourceCategory; // Mục 3, Bảng 3.1: Danh mục nguồn (e.g., Đốt nhiên liệu, Quy trình công nghiệp) (not_null)
//
//    @Column(name = "source_name", nullable = false, columnDefinition = "NVARCHAR(255)")
//    String sourceName; // Mục 3, Bảng 3.1: Tên nguồn phát thải (not_null)
//
//    @Column(name = "source_code", nullable = false, columnDefinition = "NVARCHAR(100)")
//    String sourceCode; // Mục 3, Bảng 3.1: Ký hiệu nguồn (not_null, unique per report)
//
//    @Column(name = "source_description", columnDefinition = "NVARCHAR(MAX)")
//    String sourceDescription; // Mục 3, Bảng 3.1: Mô tả nguồn (optional)
//
//    @Column(name = "ghg_emitted", precision = 12, scale = 2)
//    BigDecimal ghgEmitted; // Mục 3, Bảng 3.1: Khí nhà kính phát thải (tCO2e, optional, auto-calc sau)
//
//    @Column(name = "data_input_source", nullable = false, columnDefinition = "NVARCHAR(255)")
//    String dataInputSource; // Mục 3, Bảng 3.1: Nguồn dữ liệu đầu vào (not_null)
//
//    @Column(name = "source_unit", nullable = false, columnDefinition = "NVARCHAR(100)")
//    String sourceUnit; // Mục 3, Bảng 3.1: Đơn vị nguồn (e.g., kWh, tấn) (not_null)
//}