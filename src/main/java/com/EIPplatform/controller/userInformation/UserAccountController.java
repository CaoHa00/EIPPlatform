package com.EIPplatform.controller.userInformation;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.api.UserAccountDTO;
import com.EIPplatform.model.dto.userAccount.UserAccountCreateDTO;
import com.EIPplatform.model.dto.userAccount.UserAccountResponseDTO;
import com.EIPplatform.model.dto.userAccount.UserAccountUpdateDTO;
import com.EIPplatform.service.authentication.UserAccountInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@RestController
@RequestMapping("/api/v1/user-account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAccountController {

    UserAccountInterface userAccountInterface;

    // Lấy UserAccount theo ID
    @GetMapping
    public ApiResponse<UserAccountResponseDTO> getUserAccountById(@RequestParam("id") UUID id) {
        UserAccountResponseDTO result = userAccountInterface.findById(id);
        return ApiResponse.<UserAccountResponseDTO>builder()
                .result(result)
                .build();
    }

    // Tạo mới UserAccount
    @PostMapping("/create")
    public ApiResponse<UserAccountResponseDTO> createUserAccount(@RequestBody UserAccountCreateDTO dto) {
        UserAccountResponseDTO result = userAccountInterface.create(dto);
        return ApiResponse.<UserAccountResponseDTO>builder()
                .result(result)
                .message("User account created successfully")
                .build();
    }

    // Cập nhật UserAccount
    @PutMapping("/update")
    public ApiResponse<UserAccountResponseDTO> updateUserAccount(
            @RequestParam("id") UUID id,
            @RequestBody UserAccountUpdateDTO dto) {
        UserAccountResponseDTO result = userAccountInterface.update(id, dto);
        return ApiResponse.<UserAccountResponseDTO>builder()
                .result(result)
                .message("User account updated successfully")
                .build();
    }

    // Xóa UserAccount
    @DeleteMapping
    public ApiResponse<Void> deleteUserAccountById(@RequestParam("id") UUID id) {
        userAccountInterface.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("User account deleted successfully")
                .build();
    }
}