package com.EIPplatform.model.entity.report.report05.wastemanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @Column(name = "pop_name", columnDefinition = "NVARCHAR(500)", nullable = false)
    String popName;

    @Column(name = "cas_code", columnDefinition = "NVARCHAR(50)")
    String casCode;

    @Column(name = "import_date")
    String importDate;

    @Column(name = "import_volume")
    Double importVolume;

    @Column(name = "concentration", columnDefinition = "NVARCHAR(100)")
    String concentration;

    @Column(name = "volume_used")
    Double volumeUsed;

    @Column(name = "volume_stocked")
    Double volumeStocked;

    @Column(name = "compliance_result", columnDefinition = "NVARCHAR(255)")
    String complianceResult;
}
