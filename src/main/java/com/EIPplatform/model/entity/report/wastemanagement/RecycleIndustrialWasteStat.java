package com.EIPplatform.model.entity.report.wastemanagement;

import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "recycle_industrial_waste_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecycleIndustrialWasteStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recycle_id")
    Long recycleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;

    @Column(name = "transfer_org", length = 255, nullable = false)
    String transferOrg;

    @Column(name = "volume_cy", precision = 10, scale = 2, nullable = false)
    BigDecimal volumeCy; // ≥0

    @Column(name = "waste_type_desc", length = 255, nullable = false)
    String wasteTypeDesc;

    @Column(name = "volume_py", precision = 10, scale = 2, nullable = false)
    BigDecimal volumePy; // ≥0
}