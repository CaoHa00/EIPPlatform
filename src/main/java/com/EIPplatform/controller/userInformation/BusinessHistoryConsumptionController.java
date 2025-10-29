package com.EIPplatform.controller.userInformation;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;
import com.EIPplatform.service.userInformation.BusinessHistoryConsumptionInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/business-history-consumptions")
@RequiredArgsConstructor
public class BusinessHistoryConsumptionController {

    private final BusinessHistoryConsumptionInterface businessHistoryConsumptionService;

    // 🔹 Lấy danh sách theo BusinessDetail ID
    @GetMapping("/business/{businessDetailId}")
    public ResponseEntity<List<BusinessHistoryConsumptionDTO>> getByBusinessDetailId(@PathVariable UUID businessDetailId) {
        List<BusinessHistoryConsumptionDTO> result = businessHistoryConsumptionService.findByBusinessDetailId(businessDetailId);
        return ResponseEntity.ok(result);
    }

    // 🔹 Tạo mới bản ghi lịch sử tiêu thụ
    @PostMapping
    public ResponseEntity<BusinessHistoryConsumptionDTO> create(@RequestBody BusinessHistoryConsumptionDTO dto) {
        BusinessHistoryConsumptionDTO created = businessHistoryConsumptionService.create(dto);
        return ResponseEntity.ok(created);
    }

    // 🔹 Xóa toàn bộ lịch sử tiêu thụ theo BusinessDetail
    @DeleteMapping("/business/{businessDetailId}")
    public ResponseEntity<Void> deleteByBusinessDetailId(@PathVariable UUID businessDetailId) {
        businessHistoryConsumptionService.deleteByBusinessDetailId(businessDetailId);
        return ResponseEntity.noContent().build();
    }
}
