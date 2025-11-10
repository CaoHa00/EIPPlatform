package com.EIPplatform.model.entity.user.businessInformation;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.EIPplatform.configuration.AuditMetaData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "business_history_consumption")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class BusinessHistoryConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "business_history_consumption_id", updatable = false, nullable = false)
    UUID businessHistoryConsumptionId;

    @Column(nullable = false)
    Integer productVolumeCy;

    @Column(nullable = false)
    Integer productVolumePy;

    @Column(nullable = false, columnDefinition = "NVARCHAR(50)")
    String productUnitCy;

    @Column(nullable = false, columnDefinition = "NVARCHAR(50)")
    String productUnitPy;

    @Column(nullable = false)
    Integer fuelConsumptionCy;

    @Column(nullable = false)
    Integer fuelConsumptionPy;

    @Column(nullable = false, columnDefinition = "NVARCHAR(50)")
    String fuelUnitCy;

    @Column(nullable = false, columnDefinition = "NVARCHAR(50)")
    String fuelUnitPy;

    @Column(nullable = false)
    Integer electricityConsumptionCy;

    @Column(nullable = false)
    Integer electricityConsumptionPy;

    @Column(nullable = false)
    Integer waterConsumptionCy;

    @Column(nullable = false)
    Integer waterConsumptionPy;

    @ManyToOne
    @JoinColumn(name = "business_detail_id", nullable = false)
    @JsonBackReference(value = "businessDetail-historyConsumption-ref")
    BusinessDetail businessDetail;

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
