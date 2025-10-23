package com.EIPplatform.model.entity.user.userInformation;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.EIPplatform.configuration.AuditMetaData;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "[user_detail_id]")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    UUID userDetailId;

    @Column(nullable = false, unique = true)
    String phoneNumber;

    @Column(nullable = false)
    String location;
    
    @Column(nullable = false)
    String industrySector;

    String companyName;

    @Column(nullable = false, unique = true)
    String taxCode; // Mã số thuế cá nhân/doanh nghiệp

    //... add more fields as necessary

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
