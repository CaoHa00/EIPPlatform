package com.EIPplatform.model.dto.authentication;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.userInformation.UserDetailDTO;
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
public class UserAccountAuthenticationResponse {
    UUID userAccountId;
    String email;
    String password;
    boolean enable;
    String phoneNumber;
    String fullName;
    List<String> roles;
    UserDetailDTO userDetail;
}
