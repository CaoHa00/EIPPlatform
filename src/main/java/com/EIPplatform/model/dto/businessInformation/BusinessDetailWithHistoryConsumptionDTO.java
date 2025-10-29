package com.EIPplatform.model.dto.businessInformation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetailWithHistoryConsumptionDTO {
    BusinessDetailDTO businessDetailDTO;
      List<BusinessHistoryConsumptionDTO> userHistoryConsumptions;
}
