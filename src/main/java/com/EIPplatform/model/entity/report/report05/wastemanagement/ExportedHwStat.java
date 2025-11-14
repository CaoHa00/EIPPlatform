package com.EIPplatform.model.entity.report.report05.wastemanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;

import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "exported_hw_stats", indexes = {
        @Index(name = "idx_wm_id", columnList = "wm_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportedHwStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exported_id")
    Long exportedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_id", nullable = false)
    WasteManagementData wasteManagementData;
    
    @Column(name = "waste_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    String wasteName;
    
    @Column(name = "hw_code", columnDefinition = "NVARCHAR(50)", nullable = false)
    String hwCode;
    
    @Column(name = "basel_code", columnDefinition = "NVARCHAR(50)")
    String baselCode;

    @Column(name = "volume_kg", nullable = false)
    Double volumeKg;
    
    @Column(name = "transporter_org", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String transporterOrg;
    
    @Column(name = "overseas_processor_org", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String overseasProcessorOrg;
}
