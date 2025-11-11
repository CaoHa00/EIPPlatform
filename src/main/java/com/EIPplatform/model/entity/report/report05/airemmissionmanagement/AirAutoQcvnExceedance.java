package com.EIPplatform.model.entity.report.report05.airemmissionmanagement;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "air_auto_qcvn_exceedance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoQcvnExceedance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_auto_qcvn_exceedance_id")
    Long airAutoQcvnExceedanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "air_emission_data_id", nullable = false)
    AirEmissionData airEmissionData;

    @Column(name = "param_name", nullable = false, columnDefinition = "NVARCHAR(100)")
    String paramName;

    @Column(name = "exceed_days_count", nullable = false)
    Integer exceedDaysCount;

    @Column(name = "qcvn_limit_value", nullable = false)
    Integer qcvnLimitValue;

    @Column(name = "exceed_ratio_percent", precision = 5, scale = 2, nullable = false)
    BigDecimal exceedRatioPercent;
}
