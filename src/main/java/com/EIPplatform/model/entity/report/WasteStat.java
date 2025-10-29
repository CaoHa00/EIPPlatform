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