package com.EIPplatform.model.dto.permitshistory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermitFilterRequest {
    String permitType;
    Boolean isActive;
    LocalDate issueDateFrom;
    LocalDate issueDateTo;
    String sortBy;
    String sortDir;
}