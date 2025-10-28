package com.EIPplatform.model.entity.report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Entity
@Table(name = "waste_stats", indexes = {
        @Index(name = "idx_report_waste_type", columnList = "report_id, waste_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id", updatable = false, nullable = false)
    Integer statId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @JsonBackReference(value = "report-waste")
    Report report;

    @Column(name = "waste_type", nullable = false)
    String wasteType;

    @Column(name = "volume_cy", nullable = false, precision = 10, scale = 2)
    BigDecimal volumeCy;

    @Column(name = "receiver_org", nullable = false)
    String receiverOrg;

    @Column(name = "volume_py", precision = 10, scale = 2)
    BigDecimal volumePy;
}