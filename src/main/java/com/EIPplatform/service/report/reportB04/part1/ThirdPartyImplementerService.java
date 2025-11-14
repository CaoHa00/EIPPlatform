package com.EIPplatform.service.report.reportB04.part1;

import java.util.List;

import com.EIPplatform.model.dto.report.reportB04.part1.request.ThirdPartyImplementerCreationRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ThirdPartyImplementerUpdateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.response.ThirdPartyImplementerResponse;


public interface ThirdPartyImplementerService {

    ThirdPartyImplementerResponse create(ThirdPartyImplementerCreationRequest request);

    ThirdPartyImplementerResponse update(ThirdPartyImplementerUpdateRequest request);

    ThirdPartyImplementerResponse getById(Long id);

    List<ThirdPartyImplementerResponse> getAll();

    void delete(Long id);
}
