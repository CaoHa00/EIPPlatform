package com.EIPplatform.model.entity.report.report06.part01;
import com.EIPplatform.model.entity.products.Product;
import com.EIPplatform.model.entity.report.report06.Report06;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.legalDocs.LegalDocs;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;
/**
 * PHẦN I - Thông tin của cơ sở phải thực hiện kiểm kê khí nhà kính (BusinessInformation)
 */
@Entity
@Table(name = "business_information", indexes = {
        @Index(name = "idx_biz_info_report06", columnList = "report_06_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessInformation {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "business_information_id", updatable = false, nullable = false)
    UUID businessInformationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_06_id", nullable = false, unique = true)
    @JsonBackReference(value = "report06-business-info")
    Report06 report06; // Bidirectional backref

    @Column(name = "general_detail", columnDefinition = "NVARCHAR(MAX)") // New: general_detail (from business detail)
    String generalDetail; // Pulled from report06.businessDetail

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_rep_user_id")
    UserAccount legalRepUser; // New: legal_rep_user_id (FK user_account_id), replaces legalRepTitle

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_doc_id")
    LegalDocs legalDoc; // New: legal_doc_id (FK)

    @Column(name = "activity_type", columnDefinition = "NVARCHAR(255)") // New: activity_type (from BD)
    String activityType; // Pulled from report06.businessDetail.activityType

    @ManyToOne(fetch = FetchType.LAZY) // Assuming single FK as per design, but kept as ManyToOne; adjust to ManyToMany if needed
    @JoinColumn(name = "product_id")
    Product product; // New: product_id (FK), replaces ManyToMany mainProducts (simplified to single)
}