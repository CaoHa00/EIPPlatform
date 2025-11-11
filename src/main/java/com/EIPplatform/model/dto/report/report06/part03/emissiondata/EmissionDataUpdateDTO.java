package com.EIPplatform.model.dto.report.report06.part03.emissiondata;

import com.EIPplatform.model.dto.report.report06.part03.monthlyemissiondata.MonthlyEmissionDataUpdateDTO;
import com.EIPplatform.model.dto.report.report06.part03.uncertaintyevaluation.UncertaintyEvaluationUpdateDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmissionDataUpdateDTO {

    UUID emissionDataId; // Null nếu tạo mới

    UUID emissionSourceId; // Bắt buộc nếu tạo mới, không thay đổi nếu cập nhật

    @Valid
    List<@Valid MonthlyEmissionDataUpdateDTO> monthlyDatas;

    UncertaintyEvaluationUpdateDTO uncertaintyEvaluation;
}