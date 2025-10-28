package com.EIPplatform.model.entity.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Table(name = "report_section", indexes = {
        @Index(name = "idx_report_section_type", columnList = "report_id, section_type"),
        @Index(name = "idx_section_type", columnList = "section_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportSection {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "section_id", updatable = false, nullable = false)
    UUID sectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @JsonBackReference(value = "report-sections")
    Report report;

    @Column(name = "section_type", nullable = false)
    String sectionType;

    @Column(name = "section_data", columnDefinition = "NVARCHAR(MAX)")
    String sectionData;

    @Column(name = "version", nullable = false)
    Integer version;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "reportSection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "section-exceed")
    List<MonitorExceedance> monitorExceedances;

    @OneToMany(mappedBy = "reportSection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "section-hazard")
    List<HazardWaste> hazardWastes;

}