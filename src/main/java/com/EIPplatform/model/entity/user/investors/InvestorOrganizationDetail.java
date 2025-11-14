package com.EIPplatform.model.entity.user.investors;

import java.util.ArrayList;
import java.util.List;

import com.EIPplatform.model.entity.user.legalDoc.LegalDoc;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    List<LegalDoc> legalDocs;

    public void addLegalDoc(LegalDoc legalDoc) {
        if (legalDocs == null) {
            legalDocs = new ArrayList<>();
        }
        legalDocs.add(legalDoc);
        legalDoc.setInvestorOrganization(this);
    }

    public void removeLegalDoc(LegalDoc legalDoc) {
        legalDocs.remove(legalDoc);
        legalDoc.setInvestorOrganization(null);
    }
}
