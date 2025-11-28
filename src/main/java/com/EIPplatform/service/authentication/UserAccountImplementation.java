package com.EIPplatform.service.authentication;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.mapper.authentication.UserAccountMapper;
import com.EIPplatform.model.dto.userAccount.UserAccountCreateDTO;
import com.EIPplatform.model.dto.userAccount.UserAccountResponseDTO;
import com.EIPplatform.model.dto.userAccount.UserAccountUpdateDTO;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.authentication.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountImplementation implements UserAccountInterface {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;

    @Override
    public UserAccountResponseDTO findById(UUID id) {
        UserAccount entity = userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAccount not found"));
        return userAccountMapper.toResponseDTO(entity);
    }

    @Override
    public void deleteById(UUID id) {
        if (!userAccountRepository.existsById(id)) {
            throw new RuntimeException("UserAccount not found");
        }
        userAccountRepository.deleteById(id);
    }

    @Override
    public UserAccountResponseDTO create(UserAccountCreateDTO dto) {
        // DTO -> Entity
        UserAccount entity = userAccountMapper.toEntity(dto);
        entity = userAccountRepository.save(entity);
        return userAccountMapper.toResponseDTO(entity);
    }

    @Override
    public UserAccountResponseDTO update(UUID id, UserAccountUpdateDTO dto) {
        UserAccount entity = userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAccount not found"));
        // Cập nhật entity từ DTO
        userAccountMapper.updateFromDTO(dto, entity);
        entity = userAccountRepository.save(entity);
        return userAccountMapper.toResponseDTO(entity);
    }

    @Override
    public List<UserAccountResponseDTO> findAll() {
        return userAccountRepository.findAll().stream()
                .map(userAccountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
