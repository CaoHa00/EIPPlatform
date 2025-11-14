package com.EIPplatform.model.entity.report.report05.wastemanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;

import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "industrial_solid_waste_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndustrialSolidWasteStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "industrial_id")
    Long industrialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;
    
    @Column(name = "waste_group", columnDefinition = "NVARCHAR(255)", nullable = false)
    String wasteGroup;

    @Column(name = "volume_cy", nullable = false)
    Double volumeCy;
    
    @Column(name = "receiver_org", columnDefinition = "NVARCHAR(255)", nullable = false)
    String receiverOrg;

    @Column(name = "volume_py", nullable = false)
    Double volumePy;
}
