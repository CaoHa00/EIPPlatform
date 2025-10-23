package com.EIPplatform.model.entity.user.authentication;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners; 
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "[user_account]")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    UUID userAccountId;

    @Email
    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Builder.Default
    @Column(nullable = false)
    boolean enable = true;

    @Column(nullable = false, columnDefinition = "NVARCHAR(10)")
    String phoneNumber;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_detail_id")  // Khóa ngoại nằm ở bảng user_account
    private UserDetail userDetail;

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
