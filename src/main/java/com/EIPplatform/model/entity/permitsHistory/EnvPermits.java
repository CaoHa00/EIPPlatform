package com.EIPplatform.model.entity.permitsHistory;

import com.EIPplatform.model.entity.user.userInformation.UserDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "env_permits", indexes = {
        @Index(name = "idx_permit_number", columnList = "permit_number"),
        @Index(name = "idx_user_detail_id", columnList = "user_detail_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnvPermits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permit_id", updatable = false, nullable = false)
    Long permitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_detail_id")
    @JsonBackReference(value = "userdetail-permits")
    UserDetail userDetail;

    @Column(name = "permit_type", nullable = false)
    String permitType;

    @Column(name = "permit_number")
    String permitNumber;

    @Column(name = "project_name", nullable = false)
    String projectName;

    @Column(name = "issue_date", nullable = false)
    LocalDate issueDate;

    @Column(name = "issuer_org", nullable = false)
    String issuerOrg;

    @Column(name = "permit_file_path")
    String permitFilePath;

    @Column(name = "is_active", columnDefinition = "BIT DEFAULT 1")
    Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;
}