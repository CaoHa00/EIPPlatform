package com.EIPplatform.model.dto.report.statistics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatisticsDTO implements Serializable {

    private Long totalReports;
    private Long draftReports;
    private Long pendingReports;
    private Long underReviewReports;
    private Long approvedReports;
    private Long rejectedReports;
    private Long overdueReports;
    private BigDecimal averageCompletionPercentage;
    private Map<String, Long> reportsByType;
    private Map<Integer, Long> reportsByYear;
    private Map<String, Long> reportsByStatus;
    private Map<String, BigDecimal> completionRateByType;
}