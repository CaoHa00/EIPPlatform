package com.EIPplatform.model.entity.report.report05.wastemanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.Nationalized;

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
    @Nationalized
    @Column(name = "pop_name", columnDefinition = "NVARCHAR(500)", nullable = false)
    String popName;
    @Nationalized
    @Column(name = "cas_code", columnDefinition = "NVARCHAR(50)")
    String casCode;

    @Column(name = "import_date")
    LocalDate importDate;

    @Column(name = "import_volume", precision = 10, scale = 2)
    BigDecimal importVolume;
    @Nationalized
    @Column(name = "concentration", columnDefinition = "NVARCHAR(100)")
    String concentration;

    @Column(name = "volume_used", precision = 10, scale = 2)
    BigDecimal volumeUsed;

    @Column(name = "volume_stocked", precision = 10, scale = 2)
    BigDecimal volumeStocked;
    @Nationalized
    @Column(name = "compliance_result", columnDefinition = "NVARCHAR(255)")
    String complianceResult;
}
