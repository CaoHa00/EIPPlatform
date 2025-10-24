package com.EIPplatform.service.authentication;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.api.UserAccountDTO;

public interface UserAccountInterface {

    UserAccountDTO findById(UUID id);

    void deleteById(UUID id);

    UserAccountDTO create(UserAccountDTO dto);

    UserAccountDTO update(UUID id, UserAccountDTO dto);

    List<UserAccountDTO> findAll();
}
