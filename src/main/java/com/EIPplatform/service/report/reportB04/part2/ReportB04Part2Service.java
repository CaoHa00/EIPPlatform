package com.EIPplatform.service.report.reportB04.part2;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.products.ProductCreationListRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductCreationRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductDTO;

import antlr.collections.impl.LList;

public interface ReportB04Part2Service {

    List<ProductCreationRequest>createReportB04Part2(UUID reportId, UUID userAccountId, ProductCreationListRequest request);

    List<ProductCreationRequest> getReportB04Part2(UUID reportId, UUID userAccountId);

    List<ProductCreationRequest> getInitialReportB04Part2(UUID reportId, UUID userAccountId); // get all information that user
                                                                                  // already had.
    // void deleteReportInvestorDetailDTO(UUID reportId, UUID userAccountId);
    // boolean hasAirEmissionDataFile(UUID reportId, UUID userAccountId);
}
