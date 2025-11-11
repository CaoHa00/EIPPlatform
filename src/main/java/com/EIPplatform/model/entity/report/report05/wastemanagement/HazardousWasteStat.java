package com.EIPplatform.model.entity.report.report05.wastemanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import org.hibernate.annotations.Nationalized;

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
    @Nationalized
    @Column(name = "waste_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    String wasteName;
    @Nationalized
    @Column(name = "hw_code", columnDefinition = "NVARCHAR(50)", nullable = false)
    String hwCode;

    @Column(name = "volume_cy", precision = 10, scale = 2, nullable = false)
    BigDecimal volumeCy;
    @Nationalized
    @Column(name = "treatment_method", columnDefinition = "NVARCHAR(255)", nullable = false)
    String treatmentMethod;
    @Nationalized
    @Column(name = "receiver_org", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String receiverOrg;

    @Column(name = "volume_py", precision = 10, scale = 2, nullable = false)
    BigDecimal volumePy;
}
