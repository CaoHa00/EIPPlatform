package com.EIPplatform.controller.userInformation;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.api.UserAccountDTO;
import com.EIPplatform.service.authentication.UserAccountInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/user-account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAccountController {
    UserAccountInterface userAccountInterface;
    @GetMapping
    ApiResponse<UserAccountDTO> getUserAccountById(@RequestParam("id") UUID id) {
        var result = userAccountInterface.findById(id);
        return ApiResponse.<UserAccountDTO>builder()
                .result(result)
                .build();
    }
    @PostMapping("/create")
    ApiResponse<UserAccountDTO> createUserAccount(@RequestBody UserAccountDTO dto) {
        var result = userAccountInterface.create(dto);
        return ApiResponse.<UserAccountDTO>builder()
                .result(result)
                .message("User account created successfully")
                .build();
    }
    @DeleteMapping
    ApiResponse<Void> deleteUserAccountById(@RequestParam("id") UUID id) {
        userAccountInterface.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("User account deleted successfully")
                .build();
    }
}
