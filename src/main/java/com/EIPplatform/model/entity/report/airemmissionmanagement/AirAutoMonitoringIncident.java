package com.EIPplatform.model.entity.report.airemmissionmanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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

    @Column(name = "incident_name", length = 255, nullable = false)
    String incidentName;

    @Column(name = "incident_time", nullable = false)
    LocalDateTime incidentTime;

    @Lob
    @Column(name = "incident_remedy", nullable = false)
    String incidentRemedy;
}
