package com.EIPplatform.model.entity.report.report05.wastewatermanager;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "auto_ww_monitoring_stats", indexes = {
        @Index(name = "idx_auto_stats_ww_id", columnList = "ww_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoWWMonitoringStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    Long statId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ww_id", nullable = false)
    @JsonBackReference
    WasteWaterData wasteWaterData;

    @Column(name = "param_name", columnDefinition = "NVARCHAR(100)", nullable = false)
    String paramName;

    @Column(name = "val_design", nullable = false)
    Integer valDesign;

    @Column(name = "val_received", nullable = false)
    Integer valReceived;

    @Column(name = "val_error", nullable = false)
    Integer valError;

    @Column(name = "ratio_received_design", nullable = false)
    Double ratioReceivedDesign;

    @Column(name = "ratio_error_received", nullable = false)
    Double ratioErrorReceived;
}
