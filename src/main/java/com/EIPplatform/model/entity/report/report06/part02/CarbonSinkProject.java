package com.EIPplatform.model.entity.report.report06.part02;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * CarbonSinkProject: Bể hấp thụ KNK (Mục 3.2 Phần II - Bảng 3.2: Danh mục bể hấp thụ KNK).
 * Lưu thông tin dự án CCS (Carbon Capture and Storage) cho từng dự án, hỗ trợ multiple projects.
 */
@Entity
@Table(name = "carbon_sink_project", indexes = {
        @Index(name = "idx_carbon_sink_op_data", columnList = "operational_activity_data_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarbonSinkProject {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "carbon_sink_project_id", updatable = false, nullable = false)
    UUID carbonSinkProjectId; // ID chính (PK) của bảng carbon_sink_project

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operational_activity_data_id", nullable = false)
    @JsonBackReference(value = "op-data-carbon-sinks")
    OperationalActivityData operationalActivityData; // Liên kết ngược với OperationalActivityData (bảng operational_activity_data, Phần II chính)

    @Column(name = "project_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String projectName; // Mục 3.2, Bảng 3.2: Tên dự án CCS (not_null)

    @Column(name = "capture_technology", nullable = false, columnDefinition = "NVARCHAR(100)")
    String captureTechnology; // Mục 3.2, Bảng 3.2: Công nghệ thu hồi (e.g., Post-combustion, Pre-combustion, Oxy-fuel) (not_null)

    @Column(name = "captured_co2_amount_tons", nullable = false, precision = 12, scale = 2)
    BigDecimal capturedCo2AmountTons; // Mục 3.2, Bảng 3.2: Lượng CO2 thu hồi (tấn CO2/năm) (not_null)

    @Column(name = "storage_site_location", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String storageSiteLocation; // Mục 3.2, Bảng 3.2: Vị trí địa điểm lưu trữ (not_null)

    @Column(name = "monitoring_details", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String monitoringDetails; // Mục 3.2, Bảng 3.2: Chi tiết giám sát và xác minh (not_null)

    @Column(name = "net_co2_sequestered_tons", nullable = false, precision = 12, scale = 2)
    BigDecimal netCo2SequesteredTons; // Mục 3.2, Bảng 3.2: Lượng CO2 lưu trữ ròng (tấn CO2/năm) (not_null)
}
