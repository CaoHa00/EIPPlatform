package com.EIPplatform.controller.report.report06.part02;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataDTO;
import com.EIPplatform.service.report.report06.part02.OperationActivityDataService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/report06")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class OperationActivityDataController {
    OperationActivityDataService operationActivityDataService;

    @PostMapping(value = "/{report06Id}/draft/operation-activity")
    public ApiResponse<OperationalActivityDataDTO> createOperationActivityData(@PathVariable UUID report06Id,
                                                                               @RequestParam UUID userAccountId,
                                                                               @RequestBody @Valid OperationalActivityDataCreateDTO operationalActivityDataCreateDTO
                                                                               ){
        var result = operationActivityDataService.createOperationalActivityData(report06Id, userAccountId, operationalActivityDataCreateDTO);
        return ApiResponse.<OperationalActivityDataDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping(value = "/{report06Id}/draft/operation-activity")
    public ApiResponse<OperationalActivityDataDTO> getOperationalActivityDataDTO(@PathVariable UUID report06Id , @RequestParam UUID userAccountId){
        var result = operationActivityDataService.getOperationalActivityData(report06Id, userAccountId);
        return ApiResponse.<OperationalActivityDataDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping(value = "/{report06Id}/draft/delete")
    public ApiResponse<String> deleteOperationalActivityData(@PathVariable UUID report06Id,@RequestParam UUID userAccountId){
        operationActivityDataService.deleteOperationalActivityData(report06Id, userAccountId);
        return ApiResponse.<String>builder()
                .result("Operational Activity Data has been deleted from redis")
                .build();
    }
}
