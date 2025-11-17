package com.EIPplatform.model.entity.report.report06.part03;

import com.EIPplatform.model.entity.report.report06.Report06;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



/**
 * PHẦN III - Kết quả thực hiện kiểm kê phát thải khí nhà kính (InventoryResultData)
 *
 * Bảng chính cho Phần III, chứa metadata chung và relationships đến các bảng con.
 * - Mục 1: Các trường mô tả phương pháp (thêm vào entity chính này).
 * - Mục 2: Relationships đến EmissionData (Bảng 2.1: Nhập liệu hàng tháng cho mỗi nguồn).
 * - Mục 3: result (JSON auto-generated cho các bảng chi tiết), và OneToOne đến Result (các trường 3.1-3.4).
 * - Mục 4: Các trường 4.1-4.3 ở Result, và OneToMany đến UncertaintyEvaluation (Bảng 4.4).
 */
@Entity
@Table(name = "inventory_result_data", indexes = {
        @Index(name = "idx_inv_result_report06", columnList = "report_06_id"),
        @Index(name = "idx_inv_result_op_data", columnList = "operational_activity_data_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResultData { // Dữ liệu kết quả kiểm kê
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "inventory_result_data_id", updatable = false, nullable = false)
    UUID inventoryResultDataId; // ID chính của bảng inventory_result_data

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_06_id", nullable = false, unique = true)
    @JsonBackReference(value = "report06-inventory-result")
    Report06 report06; // Bidirectional backref: Liên kết với Report06 (bảng report_06)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operational_activity_data_id", nullable = false)
    OperationalActivityData operationalActivityData; // ManyToOne to Part II (bảng operational_activity_data): Link để FE loop qua emissionSources

    // ============================================================
    // MỤC 1: Mô tả phương pháp kiểm kê (Thêm fields trực tiếp vào entity này)
    // ============================================================
    @Column(name = "method_collect_data_sources", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String methodCollectDataSources; // Mục 1.1: method_collect_data_sources - Phương pháp thu thập số liệu hoạt động (JSON array options)

    @Column(name = "method_collect_data_sources_other", length = 255)
    String methodCollectDataSourcesOther; // Mục 1.2: method_collect_data_sources_other - Mô tả phương pháp "Khác" (VARCHAR(255))

    @Column(name = "calculation_approach", length = 100, nullable = false)
    String calculationApproach; // Mục 1.3: calculation_approach - Phương pháp luận tính toán (VARCHAR(100), dropdown options)

    @Column(name = "calculation_principle", columnDefinition = "NVARCHAR(MAX)")
    String calculationPrinciple; // Mục 1.4: calculation_principle - Nguyên tắc tính toán chung (TEXT)

    @Column(name = "source_description", columnDefinition = "NVARCHAR(MAX)")
    String sourceDescription; // Mục 1.5: source_description - Ghi chú (Mục đích sử dụng) (TEXT, mô tả EF/GWP)

    // ============================================================
    // MỤC 2: Số liệu hoạt động (Relationships đến Bảng 2.1)
    // ============================================================
    @OneToMany(mappedBy = "inventoryResultData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "inv-result-emission-data")
    @Builder.Default
    List<EmissionData> emissionDatas = new ArrayList<>(); // Mục 2: emission_data_ids - OneToMany đến EmissionData (bảng emission_data, Bảng 2.1: Nhập liệu hàng tháng cho mỗi nguồn)

    // ============================================================
    // MỤC 3: Kết quả kiểm kê (JSON auto + OneToOne đến Result cho 3.1-3.4)
    // ============================================================
    @Column(name = "result", columnDefinition = "NVARCHAR(MAX)") // Mục 3: result - Auto-generated JSON cho các bảng chi tiết (read-only, e.g., Bảng Phạm vi 1/2/3)String result;
    String result;

    @OneToOne(mappedBy = "inventoryResultData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "inv-result-result")
    Result resultEntity; // Mục 3.1-3.4: OneToOne đến Result (bảng result, các trường production_unit_name, total_production_output, intensity_ratio_result (auto), result_commentary)

    // ============================================================
    // MỤC 4: Độ tin cậy, tính đầy đủ, độ không chắc chắn (OneToMany đến Bảng 4.4)
    // ============================================================
    @OneToMany(mappedBy = "inventoryResultData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "inv-result-uncertainty")
    @Builder.Default
    List<UncertaintyEvaluation> uncertaintyEvaluations = new ArrayList<>(); // Mục 4: uncertainty_evaluation_ids - OneToMany đến UncertaintyEvaluation (bảng uncertainty_evaluation, Bảng 4.4: Đánh giá độ không chắc chắn cho từng nguồn)
}