package com.EIPplatform.model.entity.businessInformation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Project {

   @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "project_id", updatable = false, nullable = false)
    UUID projectId;
    
    @Column(name = "project_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String projectName; 
    
    @Column(name = "project_location", columnDefinition = "NVARCHAR(500)")
    String projectLocation;

    @Column(name = "project_legal_doc_type", columnDefinition = "NVARCHAR(255)")
    String projectLegalDocType; 
    
    @Column(name = "project_issuer_org", columnDefinition = "NVARCHAR(255)")
    String projectIssuerOrg; 
    
    @Column(name = "project_issue_date")
    LocalDate projectIssueDate; 

    @Column(name = "project_issue_date_latest")
    LocalDate projectIssueDateLatest; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "businessDetail-projects")
    @JoinColumn(name = "business_detail_id", insertable = false, updatable = false)
    BusinessDetail businessDetail; // relationship với BusinessDetail

}
