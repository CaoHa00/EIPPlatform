package com.EIPplatform.model.entity.report.reportB04;

import com.EIPplatform.model.entity.report.reportB04.part03.ResourcesSavingAndReduction;
import com.EIPplatform.model.entity.report.reportB04.part04.SymbiosisIndustry;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportB04 {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "report_id", updatable = false, nullable = false)
    UUID reportId;

    // ============= RELATIONSHIPS =============
    // Undirectional
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rsar_id")
    ResourcesSavingAndReduction resourcesSavingAndReduction;

    // Undirectional
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "si_id")
    SymbiosisIndustry symbiosisIndustry;
}
