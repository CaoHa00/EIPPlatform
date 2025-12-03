package com.EIPplatform.model.entity.user.authentication;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;  


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
@EntityListeners(AuditingEntityListener.class)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    UUID roleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RoleName roleName;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    @Builder.Default
    Set<UserAccount> users = new HashSet<>();

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
