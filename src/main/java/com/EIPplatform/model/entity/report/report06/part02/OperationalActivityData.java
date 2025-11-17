package com.EIPplatform.model.entity.report.report06.part02;

import com.EIPplatform.model.entity.report.report06.Report06;
import com.EIPplatform.model.entity.user.businessInformation.*;
import com.EIPplatform.model.entity.user.businessInformation.Process;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * OperationalActivityData: Thông tin hoạt động sản xuất kinh doanh (Phần II chính).
 * Chứa relationships đến các bảng con và metadata cho Mục 1-4 Phần II.
 */
@Entity
@Table(name = "operational_activity_data", indexes = {
        @Index(name = "idx_op_data_report06", columnList = "report_06_id"),
        @Index(name = "idx_op_data_scale_capacity", columnList = "scale_capacity_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationalActivityData {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "operational_activity_data_id", updatable = false, nullable = false)
    UUID operationalActivityDataId; // ID chính (PK) của bảng operational_activity_data

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_06_id", nullable = false, unique = true)
    @JsonBackReference(value = "report06-operational-data")
    Report06 report06; // Liên kết với Report06 (bảng report_06, entity chính báo cáo)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scale_capacity_id")
    ScaleCapacity scaleCapacity; // Mục 1 Phần II: Ranh giới và quy mô (liên kết với bảng scale_capacity: total_area, boundary_*)

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "op_data_facility_mapping",
            joinColumns = @JoinColumn(name = "operational_activity_data_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<Facility> facilities = new ArrayList<>(); // Mục 2 Phần II, Bảng 2.1: Danh mục cơ sở hạ tầng (ManyToMany với bảng facilities: area_name, area_type, etc.)

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "op_data_process_mapping",
            joinColumns = @JoinColumn(name = "operational_activity_data_id"),
            inverseJoinColumns = @JoinColumn(name = "process_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<Process> processes = new ArrayList<>(); // Mục 2 Phần II, Bảng 2.2: Danh mục quy trình công nghệ (ManyToMany với bảng process: process_name, process_description, process_flowchart)

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "op_data_equipment_mapping",
            joinColumns = @JoinColumn(name = "operational_activity_data_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<Equipment> equipments = new ArrayList<>(); // Mục 2 Phần II, Bảng 2.3: Danh mục thiết bị chính (ManyToMany với bảng equipment: equipment_name, equipment_specifications, etc.)

    // ============================================================
    // MỤC 3: Nguồn phát thải và Bể hấp thụ
    // ============================================================
    @OneToMany(mappedBy = "operationalActivityData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "op-data-emission-sources")
    @Builder.Default
    List<EmissionSource> emissionSources = new ArrayList<>(); // Mục 3 Phần II, Bảng 3.1: Danh mục nguồn phát thải (OneToMany với bảng emission_source: source_scope, source_category, etc.)

    @OneToMany(mappedBy = "operationalActivityData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "op-data-carbon-sinks")
    @Builder.Default
    List<CarbonSinkProject> carbonSinkProjects = new ArrayList<>(); // Mục 3.2 Phần II, Bảng 3.2: Bể hấp thụ KNK (OneToMany với bảng carbon_sink_project: project_name, capture_technology, etc.)

    // ============================================================
    // MỤC 4: Hạn chế và Hệ thống thông tin
    // ============================================================
    @OneToMany(mappedBy = "operationalActivityData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "op-data-limitations")
    @Builder.Default
    List<Limitation> limitations = new ArrayList<>(); // Mục 4 Phần II, Bảng 4.3: Hạn chế thu thập dữ liệu (OneToMany với bảng limitation: limitation_name, limitation_description, improvement_plan)

    @Column(name = "data_management_procedure", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String dataManagementProcedure; // Mục 4.1 Phần II: Quy trình quản lý dữ liệu (not_null, mô tả hệ thống thu thập/quản lý dữ liệu)

    @Column(name = "emission_factor_source", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String emissionFactorSource; // Mục 4.2 Phần II: Nguồn hệ số phát thải (not_null, liệt kê nguồn EF/GWP, e.g., IPCC, Quyết định 2626/QĐ-BTNMT)
}