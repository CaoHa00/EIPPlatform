package com.EIPplatform.model.entity.report.report05.wastemanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "self_treated_hw_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SelfTreatedHwStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "self_treated_id")
    Long selfTreatedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;
    
    @Column(name = "waste_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    String wasteName;

    @Column(name = "hw_code", length = 50, nullable = false)
    String hwCode;

    @Column(name = "volume_kg", nullable = false)
    Double volumeKg;
    
    @Column(name = "self_treatment_method", columnDefinition = "NVARCHAR(255)", nullable = false)
    String selfTreatmentMethod;
}
