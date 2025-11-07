package com.EIPplatform.model.entity.report.airemmissionmanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "air_monitoring_exceedance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirMonitoringExceedance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_monitoring_exceedance_id")
    Long airMonitoringExceedanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "air_emission_data_id", nullable = false)
    AirEmissionData airEmissionData;

    @Column(name = "point_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    String pointName;

    @Column(name = "point_symbol", length = 50, nullable = false)
    String pointSymbol;

    @Column(name = "monitoring_date", nullable = false)
    LocalDate monitoringDate;

    @Column(name = "longitude", length = 20)
    String longitude;

    @Column(name = "latitude", length = 20)
    String latitude;

    @Column(name = "exceeded_param", columnDefinition = "NVARCHAR(100)", nullable = false)
    String exceededParam;

    @Column(name = "result_value", precision = 15, scale = 5, nullable = false)
    BigDecimal resultValue;

    @Column(name = "qcvn_limit", precision = 15, scale = 5, nullable = false)
    BigDecimal qcvnLimit;
}
