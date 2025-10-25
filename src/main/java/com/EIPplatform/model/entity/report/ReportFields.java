package com.EIPplatform.model.entity.report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Builder
@Entity
@Table(name = "report_fields", indexes = {
        @Index(name = "idx_report_id_field_name", columnList = "report_id, field_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id", updatable = false, nullable = false)
    Integer fieldId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @JsonBackReference(value = "report-fields")
    Report report;

    @Column(name = "field_name", nullable = false)
    String fieldName;

    @Column(name = "field_value", columnDefinition = "NVARCHAR(MAX)")
    String fieldValue;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;
}