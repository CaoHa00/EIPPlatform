package com.EIPplatform.model.dto.authentication;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TempUserSeed {
    String email;
    String password;
    String fullName;
    String phoneNumber;
    String userCode;
    String createdBy;
    String departmentName;
    Long loginTime;
}
