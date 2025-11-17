package com.EIPplatform.service.authentication;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.userAccount.*;

public interface UserAccountInterface {

    UserAccountResponseDTO findById(UUID id);

    void deleteById(UUID id);

    UserAccountResponseDTO create(UserAccountCreateDTO dto);

    UserAccountResponseDTO update(UUID id, UserAccountUpdateDTO dto);

    List<UserAccountResponseDTO> findAll();
}
