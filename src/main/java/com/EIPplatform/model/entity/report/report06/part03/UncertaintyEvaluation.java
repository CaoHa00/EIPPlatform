package com.EIPplatform.model.entity.report.report06.part03;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

/**
 * Bảng uncertainty_evaluation: Mục 4, Bảng 4.4 - Đánh giá Độ không chắc chắn (cho từng nguồn, auto-generated rows từ emissionSources).
 * FE loop qua sources để tạo rows, fill source_code/name read-only.
 */
@Entity
@Table(name = "uncertainty_evaluation", indexes = {
        @Index(name = "idx_uncertainty_inv_result", columnList = "inventory_result_data_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UncertaintyEvaluation { // Renamed from SourceUncertaintyAssessment
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "uncertainty_evaluation_id", updatable = false, nullable = false)
    UUID uncertaintyEvaluationId; // ID chính của bảng uncertainty_evaluation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_result_data_id", nullable = false)
    @JsonBackReference(value = "inv-result-uncertainty")
    InventoryResultData inventoryResultData; // Bidirectional backref: Liên kết với InventoryResultData (bảng inventory_result_data)

    @Column(name = "source_code", columnDefinition = "NVARCHAR(100)") // Mục 4, Bảng 4.4: source_code - Ký hiệu (VARCHAR(100), read-only, auto-fill từ EmissionSource)
    String sourceCode; // From emissionSource

    @Column(name = "source_name", columnDefinition = "NVARCHAR(255)") // Mục 4, Bảng 4.4: source_name - Tên nguồn phát thải (VARCHAR(255), read-only, auto-fill từ EmissionSource)
    String sourceName; // From emissionSource

    @Column(name = "ad_uncertainty_level", length = 20, nullable = false) // Mục 4, Bảng 4.4: ad_uncertainty_level - Độ không chắc chắn (Dữ liệu hoạt động) (VARCHAR(20), bắt buộc, dropdown options)
    String adUncertaintyLevel;

    @Column(name = "ef_uncertainty_level", length = 20, nullable = false) // Mục 4, Bảng 4.4: ef_uncertainty_level - Độ không chắc chắn (Hệ số phát thải) (VARCHAR(20), bắt buộc, dropdown options)
    String efUncertaintyLevel;

    @Column(name = "combined_uncertainty", length = 20) // Mục 4, Bảng 4.4: combined_uncertainty - Độ không chắc chắn kết hợp (VARCHAR(20), auto-calculated, read-only)
    String combinedUncertainty;
}