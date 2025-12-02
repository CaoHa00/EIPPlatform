package com.EIPplatform.model.dto.userAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

// DTO dùng khi UPDATE user
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAccountUpdateDTO {

    @NotBlank
    @Size(max = 255)
    String fullName;

    @NotBlank
    @Size(max = 10)
    String phoneNumber;

    // Password optional khi update
    @Size(min = 6, max = 255)
    String password;
}
