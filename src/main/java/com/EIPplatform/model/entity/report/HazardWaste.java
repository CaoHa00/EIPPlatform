package com.EIPplatform.model.entity.report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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
    ReportSection reportSection;  // Link to dynamic section

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