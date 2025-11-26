package com.EIPplatform.service.report.reportB04.part2;

import java.util.UUID;
import com.EIPplatform.model.dto.businessInformation.products.ProductCreationListRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductListDTO;

public interface ReportB04Part2Service {

    ProductListDTO createReportB04Part2(UUID reportId, UUID businessDetailId, ProductCreationListRequest request);

    ProductListDTO getReportB04Part2(UUID reportId, UUID businessDetailId);

    //ProductListDTO getInitialReportB04Part2(UUID reportId, UUID businessDetailId); // get all information that user
                                                                                  // already had.
    // void deleteReportInvestorDetailDTO(UUID reportId, UUID userAccountId);
    // boolean hasAirEmissionDataFile(UUID reportId, UUID userAccountId);
}
