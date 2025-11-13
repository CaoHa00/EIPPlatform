package com.EIPplatform.model.entity.user.investors;

import java.util.ArrayList;
import java.util.List;

import com.EIPplatform.model.entity.user.legalDoc.LegalDoc;
import com.EIPplatform.model.enums.InvestorType;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "investor_organization_detail")
@DiscriminatorValue("ORGANIZATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorOrganizationDetail extends Investor {

    @Column(name = "organization_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String organizationName;

    @OneToMany(mappedBy = "investorOrganization", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<LegalDoc> legalDocs = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.investorType = InvestorType.ORGANIZATION;
    }


    public void addLegalDoc(LegalDoc legalDoc) {
        legalDocs.add(legalDoc);
        legalDoc.setInvestorOrganization(this);
    }

    public void removeLegalDoc(LegalDoc legalDoc) {
        legalDocs.remove(legalDoc);
        legalDoc.setInvestorOrganization(null);
    }
}