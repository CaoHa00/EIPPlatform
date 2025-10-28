package com.EIPplatform.model.entity.report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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
    Report report;

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