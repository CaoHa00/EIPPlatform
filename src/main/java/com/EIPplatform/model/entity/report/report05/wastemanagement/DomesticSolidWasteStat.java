package com.EIPplatform.model.entity.report.report05.wastemanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Entity
@Table(name = "domestic_solid_waste_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DomesticSolidWasteStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domestic_id")
    Long domesticId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;
    
    @Column(name = "waste_type_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    String wasteTypeName;

    @Column(name = "volume_cy", nullable = false)
    Double volumeCy;

    @Column(name = "unit_cy", nullable = false)
    String unitCy;
    
    @Column(name = "receiver_org", columnDefinition = "NVARCHAR(255)", nullable = false)
    String receiverOrg;

    @Column(name = "volume_py", nullable = false)
    Double volumePy;

    @Column(name = "unit_py", nullable = false)
    String unitPy;
}
