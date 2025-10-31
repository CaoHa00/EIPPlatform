package com.EIPplatform.model.entity.report.wastewatermanager;

import java.math.BigDecimal;

import com.EIPplatform.model.entity.report.WasteWaterData;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    
    @Column(name = "param_name", length = 100, nullable = false)
    String paramName; // Tên thông số (pH, TSS...)
    
    @Column(name = "val_design", nullable = false)
    Integer valDesign; // Số giá trị quan trắc theo thiết kế
    
    @Column(name = "val_received", nullable = false)
    Integer valReceived; // Số giá trị quan trắc nhận được
    
    @Column(name = "val_error", nullable = false)
    Integer valError; // Số giá trị quan trắc lỗi/bất thường
    
    @Column(name = "ratio_received_design",  nullable = false)
    Double ratioReceivedDesign; // Tỉ lệ số liệu nhận được/thiết kế (%)
    
    @Column(name = "ratio_error_received", nullable = false)
    Double ratioErrorReceived; // Tỉ lệ số liệu lỗi/nhận được (%)
}
