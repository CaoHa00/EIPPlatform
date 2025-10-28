package com.EIPplatform.controller.userInformation;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.userInformation.UserDetailDTO;
import com.EIPplatform.model.dto.userInformation.UserDetailWithHistoryConsumptionDTO;
import com.EIPplatform.service.userInformation.UserDetailInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/user-detail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailController {
    UserDetailInterface userDetailInterface;
    @PostMapping
    ApiResponse<UserDetailDTO> createUserDetail(@RequestBody UserDetailDTO dto) {
        var result = userDetailInterface.create(dto);
        return ApiResponse.<UserDetailDTO>builder()
                .result(result)
                .message("User detail created successfully")
                .build();
    }
    @GetMapping("/all")
    ApiResponse<List<UserDetailDTO>> getAllUserDetails() {
        var result = userDetailInterface.findAll();
        return ApiResponse.<List<UserDetailDTO>>builder()
                .result(result)
                .build();
    }
    @DeleteMapping
    ApiResponse<Void> deleteUserDetailById(@RequestParam("id") UUID id) {
        userDetailInterface.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("User detail deleted successfully")
                .build();
    }
    @GetMapping("by-id")
    ApiResponse<UserDetailDTO> getUserDetailById(@PathVariable("id") UUID id) {
        var result = userDetailInterface.findById(id);
        return ApiResponse.<UserDetailDTO>builder()
                .result(result)
                .build();
    }
    @GetMapping("/with-history-consumption")
    ApiResponse<UserDetailWithHistoryConsumptionDTO> getUserDetailWithHistoryConsumption(@RequestParam("id") UUID id) {
        var result = userDetailInterface.getUserDetailWithHistoryConsumption(id);
        return ApiResponse.<UserDetailWithHistoryConsumptionDTO>builder()
                .result(result)
                .build();
    }

}