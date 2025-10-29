package com.EIPplatform.model.entity.report;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Table(name = "monitor_exceedance", indexes = {
        @Index(name = "idx_section_date", columnList = "section_id, monitoring_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonitorExceedance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exceedance_id", updatable = false, nullable = false)
    Integer exceedanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonBackReference(value = "section-exceed")
    ReportSection reportSection;  // Assume ReportSection exists for dynamic sections

    @Column(name = "point_name", nullable = false)
    String pointName;

    @Column(name = "point_symbol")
    String pointSymbol;

    @Column(name = "monitoring_date", nullable = false)
    LocalDate monitoringDate;

    @Column(name = "longitude")
    String longitude;

    @Column(name = "latitude")
    String latitude;

    @Column(name = "exceeded_param", nullable = false)
    String exceededParam;

    @Column(name = "result_value", nullable = false, precision = 15, scale = 5)
    BigDecimal resultValue;

    @Column(name = "qcvn_limit", nullable = false, precision = 15, scale = 5)
    BigDecimal qcvnLimit;

    @Column(name = "section_type")
    String sectionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @JsonBackReference(value = "report-exceed")
    private Report report;

}