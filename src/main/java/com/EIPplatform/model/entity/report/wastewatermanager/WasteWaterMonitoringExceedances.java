package com.EIPplatform.model.entity.report.wastewatermanager;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.EIPplatform.model.entity.report.WasteWaterData;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Entity
@Table(name = "wastewater_monitoring_exceedances", indexes = {
    @Index(name = "idx_ww_exceedances_ww_id", columnList = "ww_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteWaterMonitoringExceedances {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exceedance_id")
    Long exceedanceId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ww_id", nullable = false)
    @JsonBackReference
    WasteWaterData wasteWaterData;
    
    @Column(name = "point_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    String pointName; // Tên điểm quan trắc
    
    @Column(name = "point_symbol", length = 50, nullable = false)
    String pointSymbol; // Ký hiệu điểm
    
    @Column(name = "monitoring_date", nullable = false)
    String monitoringDate; // Thời gian quan trắc
    
    @Column(name = "longitude", length = 20)
    String longitude; // Kinh độ
    
    @Column(name = "latitude", length = 20)
    String latitude; // Vĩ độ
    
    @Column(name = "exceeded_param", length = 100, nullable = false)
    String exceededParam; // Chỉ tiêu vượt QCVN
    
    @Column(name = "result_value",  nullable = true)
    Double resultValue; // Kết quả quan trắc
    
    @Column(name = "qcvn_limit", nullable = true)
    Double qcvnLimit; // Giá trị QCVN
}
