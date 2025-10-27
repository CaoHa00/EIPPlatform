package com.EIPplatform.service.authentication;
import java.util.List;
import java.util.UUID; 

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.mapper.authentication.UserAccountMapper;
import com.EIPplatform.model.dto.api.UserAccountDTO;
import com.EIPplatform.repository.authentication.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountImplementation implements UserAccountInterface {
    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;
    @Override
    public UserAccountDTO findById(UUID id) {
        return userAccountRepository.findById(id)
                .map(userAccountMapper::toDTO)
                .orElse(null);
    }

    @Override
    public void deleteById(UUID id) {
        userAccountRepository.deleteById(id);
    }

    @Override
    public UserAccountDTO create(UserAccountDTO dto) {
        var entity = userAccountMapper.toEntity(dto);
        entity = userAccountRepository.save(entity);
        return userAccountMapper.toDTO(entity);
    }

    @Override
    public UserAccountDTO update(UUID id, UserAccountDTO dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserAccountDTO> findAll() {
        var entities = userAccountRepository.findAll();
        return userAccountMapper.toDTOList(entities);
    }
    
}
