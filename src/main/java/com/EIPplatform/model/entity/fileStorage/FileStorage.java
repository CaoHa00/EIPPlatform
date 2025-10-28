package com.EIPplatform.model.entity.fileStorage;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.user.authentication.UserAccount;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
@Table(name = "[file_storage]")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class FileStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    UUID roleId;

    @Column(nullable = false)
    String fileName;

    @Column(nullable = false)
    String filePath;

    @Column(nullable = false)
    // object business_id later
    // many files can belong to one business object
    String businessDetailId;

    @Column(nullable = false)
    @OneToOne(mappedBy = "fileStorage") // defines who submited
    UserAccount userAccount;

    @Column(nullable = false)
    String fileType;

    @Embedded
    @Builder.Default
    AuditMetaData auditMetaData = new AuditMetaData();

    public LocalDateTime getCreatedAt() {
        return auditMetaData.getCreatedAt();
    }

    public String getCreatedBy() {
        return auditMetaData.getCreatedBy();
    }

    public Boolean getIsDeleted() {
        return auditMetaData.getIsDeleted();
    }

    public LocalDateTime getVerifiedAt() {
        return auditMetaData.getVerifiedAt();
    }

    public String getVerifiedBy() {
        return auditMetaData.getVerifiedBy();
    }

    public String getRemark() {
        return auditMetaData.getRemark();
    }

}
