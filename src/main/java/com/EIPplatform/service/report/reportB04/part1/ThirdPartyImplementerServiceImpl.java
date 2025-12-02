package com.EIPplatform.service.report.reportB04.part1;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.EIPplatform.mapper.report.reportB04.part1.ThirdPartyImplementerMapper;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ThirdPartyImplementerCreationRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ThirdPartyImplementerUpdateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.response.ThirdPartyImplementerResponse;
import com.EIPplatform.model.entity.report.reportB04.part01.ThirdPartyImplementer;
import com.EIPplatform.repository.report.reportB04.part1.ThirdPartyImplementerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThirdPartyImplementerServiceImpl implements ThirdPartyImplementerService {

    private final ThirdPartyImplementerRepository repository;
    private final ThirdPartyImplementerMapper mapper;

    @Override
    public ThirdPartyImplementerResponse create(ThirdPartyImplementerCreationRequest request) {
        ThirdPartyImplementer entity = mapper.toEntityFromCreate(request);
        ThirdPartyImplementer saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public ThirdPartyImplementerResponse update(ThirdPartyImplementerUpdateRequest request) {
        ThirdPartyImplementer entity = repository.findById(request.getTpiId())
                .orElseThrow(() -> new RuntimeException("Third Party Implementer not found"));
        mapper.updateEntityFromUpdate(request, entity);
        ThirdPartyImplementer updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public ThirdPartyImplementerResponse getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Third Party Implementer not found"));
    }

    @Override
    public List<ThirdPartyImplementerResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Third Party Implementer not found");
        }
        repository.deleteById(id);
    }

}
