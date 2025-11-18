package com.EIPplatform.model.dto.api;

import java.util.UUID;

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
public class UserAccountDTO {

    UUID userAccountId;
    String email;
    String password;
    boolean enable;
    String fullName;
    String phoneNumber;
}
