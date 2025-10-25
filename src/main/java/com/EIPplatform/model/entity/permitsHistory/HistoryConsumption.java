package com.EIPplatform.model.entity.permitsHistory;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@Entity
@Table(name = "history_consumption", indexes = {
        @Index(name = "idx_year_update_user", columnList = "year_update, user_detail_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumption_id", updatable = false, nullable = false)
    Integer consumptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_detail_id")
    @JsonBackReference(value = "userdetail-history")
    UserDetail userDetail;

    @Column(name = "year_update", nullable = false)
    Integer yearUpdate;

    @Column(name = "product_volume", nullable = false, precision = 15, scale = 2)
    BigDecimal productVolume;

    @Column(name = "product_unit", nullable = false)
    String productUnit;

    @Column(name = "fuel_consumption", nullable = false, precision = 15, scale = 2)
    BigDecimal fuelConsumption;

    @Column(name = "fuel_unit", nullable = false)
    String fuelUnit;

    @Column(name = "electricity_kwh", nullable = false, precision = 15, scale = 2)
    BigDecimal electricityKwh;

    @Column(name = "water_m3", nullable = false, precision = 15, scale = 2)
    BigDecimal waterM3;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;
}