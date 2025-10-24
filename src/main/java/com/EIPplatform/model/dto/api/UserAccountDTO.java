package com.EIPplatform.model.dto.api;

import java.util.UUID;
import com.EIPplatform.model.dto.userInformation.UserDetailDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    String phoneNumber;
    
    @JsonIgnore
    UserDetailDTO userDetail;
}
