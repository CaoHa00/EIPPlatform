package com.EIPplatform.model.entity.report.report05.wastewatermanager;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    
    @Column(name = "point_symbol", columnDefinition = "NVARCHAR(50)", nullable = false)
    String pointSymbol; // Ký hiệu điểm

    @Column(name = "monitoring_date", nullable = false)
    String monitoringDate; // Thời gian quan trắc
    
    @Column(name = "longitude", columnDefinition = "NVARCHAR(20)")
    String longitude; // Kinh độ
    
    @Column(name = "latitude", columnDefinition = "NVARCHAR(20)")
    String latitude; // Vĩ độ
    
    @Column(name = "exceeded_param", columnDefinition = "NVARCHAR(100)", nullable = false)
    String exceededParam; // Chỉ tiêu vượt QCVN

    @Column(name = "result_value")
    Double resultValue; // Kết quả quan trắc

    @Column(name = "qcvn_limit")
    Double qcvnLimit; // Giá trị QCVN
}
