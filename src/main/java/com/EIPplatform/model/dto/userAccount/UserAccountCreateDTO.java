package com.EIPplatform.model.dto.userAccount;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

// DTO dùng khi CREATE mới user
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAccountCreateDTO {
    
    @NotBlank
    @Email
    @Size(max = 255)
    String email;

    @NotBlank
    @Size(min = 6, max = 255)
    String password;

    @NotBlank
    @Size(max = 255)
    String fullName;

    @NotBlank
    @Size(max = 10)
    String phoneNumber;
}