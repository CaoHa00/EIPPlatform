
package com.EIPplatform.model.entity.report;
import java.math.BigDecimal;

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
@Table(name = "auto_mon_stats", indexes = {
        @Index(name = "idx_param_name", columnList = "param_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoMonStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id", updatable = false, nullable = false)
    Integer statId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @JsonBackReference(value = "report-auto")
    Report_A05 report;

    @Column(name = "param_name", nullable = false)
    String paramName;

    @Column(name = "val_design", nullable = false)
    Integer valDesign;

    @Column(name = "val_received", nullable = false)
    Integer valReceived;

    @Column(name = "val_error", nullable = false)
    Integer valError;

    @Column(name = "ratio_received_design", precision = 5, scale = 2)
    BigDecimal ratioReceivedDesign;

    @Column(name = "ratio_error_received", precision = 5, scale = 2)
    BigDecimal ratioErrorReceived;
}