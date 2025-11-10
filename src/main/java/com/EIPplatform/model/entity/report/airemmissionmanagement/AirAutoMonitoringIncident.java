package com.EIPplatform.model.entity.report.airemmissionmanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "air_auto_monitoring_incident")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_auto_monitoring_incident_id")
    Long airAutoMonitoringIncidentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "air_emission_data_id", nullable = false)
    AirEmissionData airEmissionData;
    @Nationalized
    @Column(name = "incident_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String incidentName;

    @Column(name = "incident_time", nullable = false)
    LocalDateTime incidentTime;
    @Nationalized
    @Lob
    @Column(name = "incident_remedy", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String incidentRemedy;
}
