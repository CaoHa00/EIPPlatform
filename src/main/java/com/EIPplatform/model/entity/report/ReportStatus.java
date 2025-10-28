package com.EIPplatform.model.entity.report;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Table(name = "report_status", indexes = {
        @Index(name = "idx_status_code", columnList = "status_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id", updatable = false, nullable = false)
    Long statusId;

    @Column(name = "status_name", nullable = false)
    String statusName;

    @Column(name = "status_code", nullable = false)
    String statusCode;

    @Column(name = "status_order")
    Integer statusOrder;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    String description;

    @OneToMany(mappedBy = "reportStatus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "status-report")
    List<Report> reports;
}