package com.EIPplatform.model.entity.report.report06.part02;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

/**
 * Limitation: Hạn chế dữ liệu (Mục 4 Phần II - Bảng 4.3: Hạn chế thu thập dữ liệu).
 * Lưu chi tiết hạn chế cho từng nguồn hoặc chung, hỗ trợ multiple limitations per report.
 */
@Entity
@Table(name = "limitation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Limitation {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "limitation_id", updatable = false, nullable = false)
    UUID limitationId; // ID chính (PK) của bảng limitation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operational_activity_data_id", nullable = false)
    @JsonBackReference(value = "op-data-limitations")
    OperationalActivityData operationalActivityData; // Liên kết ngược với OperationalActivityData (bảng operational_activity_data, Phần II chính)

    @Column(name = "limitation_name", columnDefinition = "NVARCHAR(255)")
    String limitationName; // Mục 4, Bảng 4.3: Tên hạn chế (optional)

    @Column(name = "limitation_description", columnDefinition = "NVARCHAR(MAX)")
    String limitationDescription; // Mục 4, Bảng 4.3: Mô tả hạn chế (optional)

    @Column(name = "improvement_plan", columnDefinition = "NVARCHAR(MAX)")
    String improvementPlan; // Mục 4, Bảng 4.3: Kế hoạch cải thiện (optional)
}