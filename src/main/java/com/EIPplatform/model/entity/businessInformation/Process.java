package com.EIPplatform.model.entity.businessInformation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Entity cho Process: Quy trình công nghệ (Bảng 2.2 Phần II).
 * Bảng: process
 * Referenced via ManyToMany in OperationalActivityData.
 */
@Entity
@Table(name = "process")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Process {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "process_id", updatable = false, nullable = false)
    UUID processId; // process_id (PK)

    @Column(name = "process_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String processName; // process_name (not_null, tên quy trình)

    @Column(name = "process_description", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String processDescription; // process_description (not_null, mô tả quy trình)

    @Column(name = "process_flowchart", columnDefinition = "NVARCHAR(500)")
    String processFlowchart; // process_flowchart (optional, URL/path đến sơ đồ flowchart)

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_detail_id")
    @JsonBackReference(value = "businessDetail-processes")
    private BusinessDetail businessDetail;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
