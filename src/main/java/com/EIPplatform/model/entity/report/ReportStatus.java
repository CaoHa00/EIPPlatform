package com.EIPplatform.model.entity.report;
// Trạng thái báo cáo 

import java.time.LocalDateTime;

import com.EIPplatform.configuration.AuditMetaData;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Entity
@Table(name = "report_status") // Table name in the database
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_status_id", updatable = false, nullable = false)
    Long reportStatusId;

    @Column(name = "report_status_name", nullable = false)
    String reportStatusName;

    @Embedded
    @Builder.Default
    AuditMetaData auditMetaData = new AuditMetaData();

    public LocalDateTime getCreatedAt() {
        return auditMetaData.getCreatedAt();
    }

    public String getCreatedBy() {
        return auditMetaData.getCreatedBy();
    }

    public LocalDateTime getUpdatedAt() {
        return auditMetaData.getUpdatedAt();
    }

    public String getUpdatedBy() {
        return auditMetaData.getUpdatedBy();
    }

}
