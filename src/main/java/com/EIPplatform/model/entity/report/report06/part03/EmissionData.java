//package com.EIPplatform.model.entity.report.report06.part03;
//
//import com.EIPplatform.model.entity.report.report06.part02.EmissionSource;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.UuidGenerator;
//import java.lang.Double;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Bảng emission_data: Parent cho Mục 2 - Số liệu hoạt động cho mỗi nguồn phát thải (Bảng 2.1).
// * FE loop qua emissionSources từ Part II để tạo EmissionData tương ứng.
// */
//@Entity
//@Table(name = "emission_data")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class EmissionData {
//    @Id
//    @GeneratedValue
//    @UuidGenerator
//    @Column(name = "emission_data_id", updatable = false, nullable = false)
//    UUID emissionDataID; // ID chính của bảng emission_data
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "inventory_result_data_id", nullable = false)
//    @JsonBackReference(value = "inv-result-emission-data")
//    InventoryResultData inventoryResultData; // Bidirectional backref: Liên kết với InventoryResultData (bảng inventory_result_data)
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "emission_source_id", nullable = false)
//    EmissionSource emissionSource; // Liên kết với nguồn phát thải (bảng emission_source) để FE hiển thị info nguồn (read-only)
//
//    @OneToMany(mappedBy = "emissionData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonManagedReference(value = "emission-data-monthly")
//    @Builder.Default
//    List<MonthlyEmissionData> monthlyDatas = new ArrayList<>(); // Mục 2, Bảng 2.1: OneToMany đến MonthlyEmissionData (bảng monthly_emission_data, month_1 đến month_12 + notes)
//
//    @Column(name = "total_annual_data", precision = 16) // Mục 2.2: total_annual_data - Tổng dữ liệu hoạt động (Cả năm) (DECIMAL(16,2), auto SUM từ monthlyDatas, read-only)
//    Double totalAnnualData;
//}