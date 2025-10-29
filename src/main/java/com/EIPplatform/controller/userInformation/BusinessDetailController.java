package com.EIPplatform.controller.userInformation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;
import com.EIPplatform.service.userInformation.BusinessDetailInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/business-details")
@RequiredArgsConstructor
public class BusinessDetailController {

    private final BusinessDetailInterface businessDetailService;

    // 🔹 Lấy toàn bộ danh sách (tạm chưa hỗ trợ)
    @GetMapping
    public ResponseEntity<List<BusinessDetailDTO>> getAllBusinessDetails() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // 🔹 Lấy thông tin business detail theo ID
    @GetMapping("/{id}")
    public ResponseEntity<BusinessDetailDTO> getBusinessDetailById(@PathVariable UUID id) {
        BusinessDetailDTO dto = businessDetailService.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // 🔹 Lấy thông tin business detail kèm lịch sử tiêu thụ (ví dụ điện/nước)
    @GetMapping("/{id}/with-history")
    public ResponseEntity<BusinessDetailWithHistoryConsumptionDTO> getBusinessDetailWithHistory(
            @PathVariable UUID id) {
        BusinessDetailWithHistoryConsumptionDTO dto = businessDetailService.getBusinessDetailWithHistoryConsumption(id);
        return ResponseEntity.ok(dto);
    }

    // 🔹 Tạo mới business detail
    @PostMapping
    public ResponseEntity<BusinessDetailDTO> createBusinessDetail(@RequestBody BusinessDetailDTO dto) {
        BusinessDetailDTO created = businessDetailService.create(dto);
        return ResponseEntity.ok(created);
    }

    // 🔹 Cập nhật business detail (tạm chưa hỗ trợ)
    @PutMapping("/{id}")
    public ResponseEntity<BusinessDetailDTO> updateBusinessDetail(@PathVariable UUID id,
                                                                  @RequestBody BusinessDetailDTO dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // 🔹 Xóa business detail
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessDetail(@PathVariable UUID id) {
        businessDetailService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
