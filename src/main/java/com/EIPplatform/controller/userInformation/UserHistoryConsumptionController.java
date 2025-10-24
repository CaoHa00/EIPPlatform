package com.EIPplatform.controller.userInformation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.service.userInformation.UserHistoryConsumptionInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/history-consumption")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserHistoryConsumptionController {
    UserHistoryConsumptionInterface userHistoryConsumptionInterface;
    @DeleteMapping
    ApiResponse<Void> deleteHistoryConsumptionById(Long id) {
        userHistoryConsumptionInterface.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("History consumption deleted successfully")
                .build();
    }
}
