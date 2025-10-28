package com.EIPplatform.model.entity.report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Builder
@Entity
@Table(name = "report_type", indexes = {
        @Index(name = "idx_report_name", columnList = "report_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_type_id", updatable = false, nullable = false)
    Integer reportTypeId;

    @Column(name = "report_name", nullable = false)
    String reportName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_template_id")
    @JsonBackReference(value = "template-type")
    ReportTemplate reportTemplate;

    @Column(name = "due_date", nullable = false)
    LocalDate dueDate;

    @Column(name = "frequency")
    String frequency;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    String description;

    @OneToMany(mappedBy = "reportType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "type-report")
    List<Report> reports;
}