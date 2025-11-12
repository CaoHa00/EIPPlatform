package com.EIPplatform.model.entity.report.report05.wastewatermanager;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "auto_ww_monitoring_incidents", indexes = {
        @Index(name = "idx_auto_stats_ww_id", columnList = "ww_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoWWMonitoringIncidents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incident_id")
    Long incidentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ww_id", nullable = false)
    @JsonBackReference
    WasteWaterData wasteWaterData;
    
    @Column(name = "incident_name", columnDefinition = "NVARCHAR(200)", nullable = false)
    String incidentName;
    @Nationalized
    @Column(name = "incident_time", nullable = false, columnDefinition = "NVARCHAR(255)")
    String incidentTime;
    @Nationalized
    @Column(name = "incident_remedy", columnDefinition = "NVARCHAR(255)", nullable = false)
    String incidentRemedy;
}
