package com.EIPplatform.model.entity.businessInformation.products;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", columnDefinition = "uniqueidentifier")
    UUID productId;

    @Column(name = "product_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String productName;

    @Column(name = "product_unit", nullable = false, columnDefinition = "NVARCHAR(50)")
    String productUnit;

    @Column(name = "product_designed_capacity", nullable = false, columnDefinition = "NVARCHAR(100)")
    String productDesignedCapacity;

    @Column(name = "product_actual_output", nullable = false, columnDefinition = "NVARCHAR(100)")
    String productActualOutput;

    @Column(name = "product_energy_consumed_sources", nullable = false, columnDefinition = "NVARCHAR(500)")
    String productEnergyConsumedSources;

    @Column(name = "product_image", columnDefinition = "NVARCHAR(MAX)")
    String productImage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_detail_id", nullable = false)
    @JsonBackReference(value = "businessDetail-products")
    BusinessDetail businessDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_b04_id", nullable = true, unique = true)
    @JsonBackReference(value = "report-productId-ref")
    ReportB04 reportB04;

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