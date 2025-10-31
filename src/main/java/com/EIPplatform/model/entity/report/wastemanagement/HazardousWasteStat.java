package com.EIPplatform.model.entity.report.wastemanagement;

import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "hazardous_waste_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HazardousWasteStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hazardous_id")
    Long hazardousId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;

    @Column(name = "waste_name", length = 255, nullable = false)
    String wasteName;

    @Column(name = "hw_code", length = 50, nullable = false)
    String hwCode;

    @Column(name = "volume_cy", precision = 10, scale = 2, nullable = false)
    BigDecimal volumeCy; // ≥0

    @Column(name = "treatment_method", length = 255, nullable = false)
    String treatmentMethod; // Multi-select as comma-separated

    @Column(name = "receiver_org", columnDefinition = "TEXT", nullable = false)
    String receiverOrg;

    @Column(name = "volume_py", precision = 10, scale = 2, nullable = false)
    BigDecimal volumePy; // ≥0
}