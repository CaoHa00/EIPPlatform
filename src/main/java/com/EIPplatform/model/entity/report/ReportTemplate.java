package com.EIPplatform.model.entity.report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Table(name = "report_template", indexes = {
        @Index(name = "idx_template_code", columnList = "template_code", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_template_id", updatable = false, nullable = false)
    Integer reportTemplateId;

    @Column(name = "template_code", nullable = false, unique = true)
    String templateCode;

    @Column(name = "template_name", nullable = false)
    String templateName;

    @Column(name = "template_version", nullable = false)
    Integer templateVersion;

    @Column(name = "schema_definition", columnDefinition = "NVARCHAR(MAX)")
    String schemaDefinition;

    @Column(name = "is_active", columnDefinition = "BIT DEFAULT 1")
    Boolean isActive;

    @Column(name = "effective_from")
    LocalDate effectiveFrom;

    @OneToMany(mappedBy = "reportTemplate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "template-type")
    List<ReportType> reportTypes;
}