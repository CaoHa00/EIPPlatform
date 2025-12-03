package com.EIPplatform.model.entity.report.report05.airemmissionmanagement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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

    @Column(name = "incident_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String incidentName;

    @Column(name = "incident_time", nullable = false)
    String incidentTime;

    @Lob
    @Column(name = "incident_remedy", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String incidentRemedy;
}
