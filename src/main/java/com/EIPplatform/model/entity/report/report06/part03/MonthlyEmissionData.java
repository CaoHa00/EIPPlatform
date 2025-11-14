//package com.EIPplatform.model.entity.report.report06.part03;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.UuidGenerator;
//
//import java.util.UUID;
//
///**
// * Bảng monthly_emission_data: Child của EmissionData cho Mục 2, Bảng 2.1 - Dữ liệu hàng tháng (set-cứng 12 dòng).
// * Mỗi record đại diện cho 1 tháng (month=1-12), value cho month_X, notes cho notes_X.
// */
//@Entity
//@Table(name = "monthly_emission_data")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class MonthlyEmissionData {
//    @Id
//    @GeneratedValue
//    @UuidGenerator
//    @Column(name = "monthly_emission_data", updatable = false, nullable = false)
//    UUID id; // ID chính của bảng monthly_emission_data
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "emission_data_id", nullable = false)
//    @JsonBackReference(value = "emission-data-monthly")
//    EmissionData emissionData; // Backref to parent: Liên kết với EmissionData (bảng emission_data)
//
//    @Column(name = "month", nullable = false) // Mục 2, Bảng 2.1: month - Tháng (Integer 1-12)
//    Integer month;
//
//    @Column(name = "value", precision = 14, nullable = false) // Mục 2, Bảng 2.1: month_X (e.g., month_1) - Giá trị tháng (DECIMAL(14,2), bắt buộc >=0)
//    Double value; // Monthly value
//
//    @Column(name = "notes", columnDefinition = "NVARCHAR(500)") // Mục 2, Bảng 2.1: notes_X (e.g., notes_1) - Ghi chú tháng (VARCHAR(500), optional)
//    String notes;
//}