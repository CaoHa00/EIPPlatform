package com.EIPplatform.model.entity.report.report05.airemmissionmanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;

import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "air_auto_monitoring_stat")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_auto_monitoring_stat_id")
    Long airAutoMonitoringStatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "air_emission_data_id", nullable = false)
    AirEmissionData airEmissionData;
    
    @Column(name = "param_name", nullable = false, columnDefinition = "NVARCHAR(100)")
    String paramName;

    @Column(name = "val_design", nullable = false)
    Integer valDesign;

    @Column(name = "val_received", nullable = false)
    Integer valReceived;

    @Column(name = "val_error", nullable = false)
    Integer valError;

    @Column(name = "ratio_received_design",  nullable = false)
    Double ratioReceivedDesign;

    @Column(name = "ratio_error_received", nullable = false)
    Double ratioErrorReceived;
}
