package com.EIPplatform.model.entity.report.wastemanagement;

import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "other_solid_waste_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherSolidWasteStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "other_id")
    Long otherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;

    @Column(name = "waste_group_other", length = 255, nullable = false)
    String wasteGroupOther;

    @Column(name = "volume_cy", precision = 10, scale = 2, nullable = false)
    BigDecimal volumeCy; // ≥0

    @Column(name = "self_treatment_method", length = 255)
    String selfTreatmentMethod;

    @Column(name = "receiver_org", length = 255)
    String receiverOrg;

    @Column(name = "volume_py", precision = 10, scale = 2, nullable = false)
    BigDecimal volumePy; // ≥0
}