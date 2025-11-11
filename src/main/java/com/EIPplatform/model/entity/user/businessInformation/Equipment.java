//package com.EIPplatform.model.entity.user.businessInformation;
//
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.UuidGenerator;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.UUID;
///**
// * Entity cho Equipment: Thiết bị chính (Bảng 2.3 Phần II).
// * Bảng: equipment
// * Referenced via ManyToMany in OperationalActivityData.
// */
//@Entity
//@Table(name = "equipment")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class Equipment {
//    @Id
//    @GeneratedValue
//    @UuidGenerator
//    @Column(name = "equipment_id", updatable = false, nullable = false)
//    UUID equipmentId; // equipment_id (PK)
//
//    @Column(name = "equipment_name", nullable = false, columnDefinition = "NVARCHAR(255)")
//    String equipmentName; // equipment_name (not_null, tên thiết bị)
//
//    @Column(name = "equipment_specifications", nullable = false, columnDefinition = "NVARCHAR(MAX)")
//    String equipmentSpecifications; // equipment_specifications (not_null, thông số kỹ thuật)
//
//    @Column(name = "equipment_quantity", nullable = false)
//    Integer equipmentQuantity; // equipment_quantity (not_null, số lượng)
//
//    @Column(name = "equipment_fuel_type", columnDefinition = "NVARCHAR(100)")
//    String equipmentFuelType; // equipment_fuel_type (optional, loại nhiên liệu)
//
//    @Column(name = "equipment_purpose", nullable = false, columnDefinition = "NVARCHAR(255)")
//    String equipmentPurpose; // equipment_purpose (not_null, mục đích sử dụng)
//
//    @Column(name = "equipment_condition", precision = 5, scale = 2) // equipment_condition (%) - Phần trăm tình trạng (e.g., 95.00%)
//    BigDecimal equipmentCondition;
//
//    @Column(name = "equipment_origin", columnDefinition = "NVARCHAR(255)")
//    String equipmentOrigin; // equipment_origin (optional, nguồn gốc thiết bị)
//
//    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
//    LocalDateTime createdAt;
//
//    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
//    LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
//}