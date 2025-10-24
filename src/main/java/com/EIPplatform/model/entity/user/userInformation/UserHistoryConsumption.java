package com.EIPplatform.model.entity.user.userInformation;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "user_history_consumption") // Table name in the database
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserHistoryConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_history_consumption_id", updatable = false, nullable = false)
    Long userHistoryConsumptionId;

    @Column(nullable = false)
    String productVolume;

    @Column(nullable = false)
    String productUnit;

    @Column(nullable = false)
    String fuelConsumption;;

    @Column(nullable = false)
    String fuelUnit; // (Litres / Kilograms / Tons)

    @Column(nullable = false)
    String electricityConsumption; // kWh

    @Column(nullable = false)
    String waterConsumption; // m3

    @Column(nullable = false)
    String yearUpdated;

    @ManyToOne
    @JoinColumn(name = "user_detail_id", nullable = false)
    @JsonBackReference(value = "userDetailsHistoryConsumption-ref")
    UserDetail userDetail;

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
