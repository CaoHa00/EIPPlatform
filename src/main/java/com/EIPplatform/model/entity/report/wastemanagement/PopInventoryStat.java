package com.EIPplatform.model.entity.report.wastemanagement;

import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pop_inventory_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopInventoryStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pop_inventory_id")
    Long popInventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;

    @Column(name = "pop_name", length = 500, nullable = false)
    String popName;

    @Column(name = "cas_code", length = 50)
    String casCode;

    @Column(name = "import_date")
    LocalDate importDate;

    @Column(name = "import_volume", precision = 10, scale = 2)
    BigDecimal importVolume; // ≥0

    @Column(name = "concentration", length = 100)
    String concentration;

    @Column(name = "volume_used", precision = 10, scale = 2)
    BigDecimal volumeUsed; // ≥0

    @Column(name = "volume_stocked", precision = 10, scale = 2)
    BigDecimal volumeStocked; // ≥0

    @Column(name = "compliance_result", length = 255)
    String complianceResult;
}