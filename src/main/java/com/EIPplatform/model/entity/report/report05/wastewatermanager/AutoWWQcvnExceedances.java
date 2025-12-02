package com.EIPplatform.model.entity.report.report05.wastewatermanager;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "auto_ww_qcvn_exceedances", indexes = {
        @Index(name = "idx_auto_qcvn_ww_id", columnList = "ww_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoWWQcvnExceedances {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qcvn_exceed_id")
    Long qcvnExceedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ww_id", nullable = false)
    @JsonBackReference
    WasteWaterData wasteWaterData;
    
    @Column(name = "param_name", columnDefinition = "NVARCHAR(100)", nullable = false)
    String paramName; // Tên thông số

    @Column(name = "exceed_days_count", nullable = false)
    Integer exceedDaysCount; // Số ngày có giá trị TB 1 giờ vượt QCVN

    @Column(name = "qcvn_limit_value", nullable = false)
    Double qcvnLimitValue; // Giá trị QCVN

    @Column(name = "exceed_ratio_percent", nullable = false)
    Double exceedRatioPercent; // Tỷ lệ giá trị TB 1 giờ vượt QCVN (%)
}
