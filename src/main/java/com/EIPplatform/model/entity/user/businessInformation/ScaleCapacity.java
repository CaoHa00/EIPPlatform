package com.EIPplatform.model.entity.user.businessInformation;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity cho ScaleCapacity: Ranh giới và quy mô cơ sở (Mục 1 Phần II).
 * Bảng: scale_capacity
 * Referenced via ManyToOne in OperationalActivityData.
 */
@Entity
@Table(name = "scale_capacity")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScaleCapacity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "scale_capacity_id", updatable = false, nullable = false)
    UUID scaleCapacityId; // scale_capacity_id (PK, not_null)

    @Column(name = "total_area", nullable = false, precision = 12, scale = 2)
    BigDecimal totalArea; // total_area (not_null, diện tích tổng, e.g., m²)

    @Column(name = "boundary_north", columnDefinition = "NVARCHAR(500)")
    String boundaryNorth; // boundary_north (optional, mô tả ranh giới phía Bắc)

    @Column(name = "boundary_south", columnDefinition = "NVARCHAR(500)")
    String boundarySouth; // boundary_south (optional, mô tả ranh giới phía Nam)

    @Column(name = "boundary_east", columnDefinition = "NVARCHAR(500)")
    String boundaryEast; // boundary_east (optional, mô tả ranh giới phía Đông)

    @Column(name = "boundary_west", columnDefinition = "NVARCHAR(500)")
    String boundaryWest; // boundary_west (optional, mô tả ranh giới phía Tây)

    @Column(name = "boundary_map_image", columnDefinition = "NVARCHAR(500)")
    String boundaryMapImage; // boundary_map_image (optional, URL/path đến hình ảnh bản đồ ranh giới)

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;

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