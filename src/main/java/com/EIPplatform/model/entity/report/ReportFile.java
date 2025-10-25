package com.EIPplatform.model.entity.report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Builder
@Entity
@Table(name = "report_files", indexes = {
        @Index(name = "idx_report_file_name", columnList = "report_id, file_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", updatable = false, nullable = false)
    Integer fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @JsonBackReference(value = "report-files")
    Report report;

    @Column(name = "file_name", nullable = false)
    String fileName;

    @Column(name = "file_path", nullable = false)
    String filePath;

    @Column(name = "upload_date", nullable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime uploadDate;
}