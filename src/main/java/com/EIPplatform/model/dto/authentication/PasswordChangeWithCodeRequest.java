package com.EIPplatform.model.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeWithCodeRequest {

    @NotBlank(message = "FIELD_REQUIRED")
    @Email(message = "EMAIL_FORMAT")
    String email;

    @NotBlank(message = "FIELD_REQUIRED")
    String verificationCode;

    @NotBlank(message = "FIELD_REQUIRED")
    @Pattern(regexp = "^\\S{8,}$", message = "FIELD_MIN_LENGTH:8")
    String newPassword;

    @NotBlank(message = "FIELD_REQUIRED")
    @Pattern(regexp = "^\\S{8,}$", message = "FIELD_MIN_LENGTH:8")
    String confirmNewPassword;
}
