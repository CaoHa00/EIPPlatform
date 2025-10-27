package com.EIPplatform.model.dto.userInformation;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailWithHistoryConsumptionDTO {
      UserDetailDTO userDetail;
      List<UserHistoryConsumptionDTO> userHistoryConsumptions;
}
