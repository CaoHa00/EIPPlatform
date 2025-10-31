package com.EIPplatform.model.entity.report.wastemanagement;

import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "exported_hw_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportedHwStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exported_id")
    Long exportedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;

    @Column(name = "waste_name", length = 255, nullable = false)
    String wasteName;

    @Column(name = "hw_code", length = 50, nullable = false)
    String hwCode;

    @Column(name = "basel_code", length = 50)
    String baselCode;

    @Column(name = "volume_kg", precision = 10, scale = 2, nullable = false)
    BigDecimal volumeKg; // ≥0

    @Column(name = "transporter_org", columnDefinition = "TEXT", nullable = false)
    String transporterOrg;

    @Column(name = "overseas_processor_org", columnDefinition = "TEXT", nullable = false)
    String overseasProcessorOrg;
}