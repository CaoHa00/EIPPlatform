package com.EIPplatform.model.entity.report.report05.wastemanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import org.hibernate.annotations.Nationalized;

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
    @Nationalized
    @Column(name = "waste_group_other", columnDefinition = "NVARCHAR(255)", nullable = false)
    String wasteGroupOther;

    @Column(name = "volume_cy", precision = 10, scale = 2, nullable = false)
    BigDecimal volumeCy;
    @Nationalized
    @Column(name = "self_treatment_method", columnDefinition = "NVARCHAR(255)")
    String selfTreatmentMethod;
    @Nationalized
    @Column(name = "receiver_org", columnDefinition = "NVARCHAR(255)")
    String receiverOrg;

    @Column(name = "volume_py", precision = 10, scale = 2, nullable = false)
    BigDecimal volumePy;
}
