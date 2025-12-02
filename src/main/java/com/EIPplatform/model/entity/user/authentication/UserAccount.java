package com.EIPplatform.model.entity.user.authentication;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Nationalized;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.fileStorage.FileStorage;
import com.EIPplatform.model.entity.user.userInformation.UserProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "user_account")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    UUID userAccountId;
    
    @Nationalized
    @Email
    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    String email;

    @Nationalized
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String password;
    
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String fullName;

    @Builder.Default
    @Column(nullable = false)
    boolean enable = true;
    
    @Column(nullable = false, columnDefinition = "NVARCHAR(50)")
    String phoneNumber;

    @OneToOne
    @JoinColumn(name = "file_storage_id")
    private FileStorage fileStorage;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference(value = "userAccountUserProfile-ref")
    private UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "business_detail_id")
    @JsonBackReference(value = "businessDetail-account-ref")
    BusinessDetail businessDetail;

    @Embedded
    @Builder.Default
    AuditMetaData auditMetaData = new AuditMetaData();
}
