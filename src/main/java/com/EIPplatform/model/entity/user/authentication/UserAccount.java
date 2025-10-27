package com.EIPplatform.model.entity.user.authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.entity.report.Report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import org.hibernate.annotations.UuidGenerator;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;

@Builder
@Entity
@Table(name = "user_account", indexes = {
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_role_id", columnList = "role_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAccount {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "user_account_id", updatable = false, nullable = false)
    UUID userAccountId;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonBackReference(value = "role-users")
    Role role;

    @Column(name = "is_verified", columnDefinition = "BIT DEFAULT 0")
    Boolean isVerified;

    @Column(name = "is_active", columnDefinition = "BIT DEFAULT 1")
    Boolean isActive;

    @Column(name = "last_login")
    LocalDateTime lastLogin;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "useraccount-detail")
    UserDetail userDetail;

    @OneToMany(mappedBy = "submittedBy", fetch = FetchType.LAZY)
    List<Report> submittedReports;

    @OneToMany(mappedBy = "reviewedBy", fetch = FetchType.LAZY)
    List<Report> reviewedReports;
}