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
@Table(name = "hazardous_waste_stats", indexes = {
        @Index(name = "idx_report_hw_code", columnList = "report_id, hw_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HazardWaste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hw_stat_id", updatable = false, nullable = false)
    Integer hwStatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonBackReference(value = "section-hazard")
    ReportSection reportSection;

    @Column(name = "waste_name", nullable = false)
    String wasteName;

    @Column(name = "hw_code", nullable = false)
    String hwCode;

    @Column(name = "volume_cy", nullable = false, precision = 10, scale = 2)
    BigDecimal volumeCy;

    @Column(name = "treatment_method", nullable = false)
    String treatmentMethod;

    @Column(name = "receiver_org", columnDefinition = "NVARCHAR(MAX)")
    String receiverOrg;

    @Column(name = "volume_py", precision = 10, scale = 2)
    BigDecimal volumePy;

    @Column(name = "section_type",  columnDefinition = "NVARCHAR(MAX)")
    String sectionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @JsonBackReference(value = "report-hazard")
    Report report;

}