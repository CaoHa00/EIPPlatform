package com.EIPplatform.model.entity.user.businessInformation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity cho Facility: Cơ sở hạ tầng (Bảng 2.1 Phần II).
 * Bảng: facilities
 * Referenced via ManyToMany in OperationalActivityData.
 */
@Entity
@Table(name = "facilities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Facility {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "facility_id", updatable = false, nullable = false)
    UUID facilityId; // facilities_id (PK)

    @Column(name = "area_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String areaName; // area_name (not_null, tên khu vực)

    @Column(name = "area_type", columnDefinition = "NVARCHAR(100)")
    String areaType; // area_type (optional, loại khu vực, e.g., sản xuất, kho bãi)

    @Column(name = "area_function", columnDefinition = "NVARCHAR(255)")
    String areaFunction; // area_function (optional, chức năng khu vực)

    @Column(name = "floor_count", nullable = false)
    Integer floorCount; // floor_count (số tầng, optional nhưng spec không chỉ rõ nullable)

    @Column(name = "build_area", precision = 12, scale = 2)
    BigDecimal buildArea; // build_area (diện tích xây dựng, m²)

    @Column(name = "floor_area", precision = 12, scale = 2)
    BigDecimal floorArea; // floor_area (diện tích sàn, m²)

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "businessDetail-facilities")
    @JoinColumn(name = "business_detail_id")
    private BusinessDetail businessDetail;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}