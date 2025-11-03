package com.EIPplatform.model.entity.report.airemmissionmanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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

    @Column(name = "param_name", length = 100, nullable = false)
    String paramName;

    @Column(name = "val_design", nullable = false)
    Integer valDesign;

    @Column(name = "val_received", nullable = false)
    Integer valReceived;

    @Column(name = "val_error", nullable = false)
    Integer valError;

    @Column(name = "ratio_received_design", precision = 5, scale = 2, nullable = false)
    BigDecimal ratioReceivedDesign;

    @Column(name = "ratio_error_received", precision = 5, scale = 2, nullable = false)
    BigDecimal ratioErrorReceived;
}
