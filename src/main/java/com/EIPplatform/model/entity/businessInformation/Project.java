package com.EIPplatform.model.entity.businessInformation;

import com.EIPplatform.model.entity.legalDoc.LegalDoc;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID projectId;

    @Column(nullable = false)
    private String projectName;

    private String projectLocation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Builder.Default
    private List<LegalDoc> legalDocs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "businessDetail-projects")
    @JoinColumn(name = "business_detail_id")
    private BusinessDetail businessDetail;

}
