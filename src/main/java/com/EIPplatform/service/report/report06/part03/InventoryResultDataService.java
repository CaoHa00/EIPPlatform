package com.EIPplatform.service.report.report06.part03;


import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataDTO;

import java.util.UUID;


public interface InventoryResultDataService {

    InventoryResultDataDTO createInventoryResultData(UUID report06Id, UUID userAccountId, InventoryResultDataCreateDTO request);

    InventoryResultDataDTO getInventoryResultData(UUID report06Id, UUID userAccountId);

    void deleteInventoryResultData(UUID report06Id, UUID userAccountId);
}