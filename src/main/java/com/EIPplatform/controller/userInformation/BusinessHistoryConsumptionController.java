package com.EIPplatform.controller.userInformation;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionCreateDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionUpdateDTO;
import com.EIPplatform.service.userInformation.BusinessHistoryConsumptionInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/business-history-consumptions")
@RequiredArgsConstructor
public class BusinessHistoryConsumptionController {

        private final BusinessHistoryConsumptionInterface businessHistoryConsumptionService;

        @GetMapping("/business/{businessDetailId}")
        public ResponseEntity<ApiResponse<List<BusinessHistoryConsumptionDTO>>> getByBusinessDetailId(
                        @PathVariable UUID businessDetailId,
                        @RequestParam UUID userAccountId) {
                var result = businessHistoryConsumptionService.findByBusinessDetailId(businessDetailId, userAccountId);
                return ResponseEntity.ok(ApiResponse.<List<BusinessHistoryConsumptionDTO>>builder()
                                .result(result)
                                .build());
        }

        @PostMapping("/business/{userAccountId}")
        public ResponseEntity<ApiResponse<BusinessHistoryConsumptionDTO>> create(
                        @PathVariable UUID userAccountId,
                        @RequestBody @Valid BusinessHistoryConsumptionCreateDTO dto) {
                var result = businessHistoryConsumptionService.createBusinessHistoryConsumption(userAccountId, dto);
                return ResponseEntity.ok(ApiResponse.<BusinessHistoryConsumptionDTO>builder()
                                .result(result)
                                .build());
        }

        @PutMapping("/{businessHistoryConsumptionId}/user/{userAccountId}")
        public ResponseEntity<ApiResponse<BusinessHistoryConsumptionDTO>> update(
                        @PathVariable UUID businessHistoryConsumptionId,
                        @RequestBody @Valid BusinessHistoryConsumptionUpdateDTO dto,
                        @PathVariable UUID userAccountId) {
                var result = businessHistoryConsumptionService.updateBusinessHistoryConsumption(userAccountId,
                                businessHistoryConsumptionId, dto);
                return ResponseEntity.ok(ApiResponse.<BusinessHistoryConsumptionDTO>builder()
                                .result(result)
                                .build());
        }

}