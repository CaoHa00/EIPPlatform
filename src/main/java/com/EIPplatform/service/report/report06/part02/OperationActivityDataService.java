package com.EIPplatform.service.report.report06.part02;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataDTO;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataUpdateDTO;

import java.util.UUID;
public interface OperationActivityDataService {

    OperationalActivityDataDTO createOperationalActivityData(UUID report06Id, UUID userAccountId, OperationalActivityDataCreateDTO request);

    OperationalActivityDataDTO getOperationalActivityData(UUID report06Id, UUID userAccountId);

    void deleteOperationalActivityData(UUID report06Id, UUID userAccountId);
}