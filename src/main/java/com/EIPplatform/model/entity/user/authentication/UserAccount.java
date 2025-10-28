package com.EIPplatform.model.entity.user.authentication;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.EIPplatform.model.entity.fileStorage.FileStorage;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.user.userInformation.BusinessDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;

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

    @Column(nullable = false)
    String fullName;

    @Builder.Default
    @Column(nullable = false)
    boolean enable = true;

    @Column(nullable = false, columnDefinition = "NVARCHAR(10)")
    String phoneNumber;

    @OneToOne
    @JoinColumn(name = "file_storage_id")
    private FileStorage fileStorage;


    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_detail_id")
    @JsonBackReference(value = "userDetailsAccount-ref")
    UserDetail userDetail;

    @ManyToOne
    @JoinColumn(name = "business_detail_id")
    @JsonBackReference(value = "businessDetail-account-ref")
    BusinessDetail businessDetail;

    @Embedded
    @Builder.Default
    AuditMetaData auditMetaData = new AuditMetaData();
}
