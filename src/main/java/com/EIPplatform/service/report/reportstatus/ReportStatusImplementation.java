package com.EIPplatform.service.report.reportstatus;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.ReportStatusMapper;
import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import com.EIPplatform.model.entity.report.ReportStatus;
import com.EIPplatform.repository.report.reportstatus.ReportStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportStatusImplementation implements ReportStatusInterface {

    private final ReportStatusRepository repository;
    private final ReportStatusMapper mapper;
    private final ExceptionFactory exceptionFactory;

    @Override
    @Transactional(readOnly = true)
    public ReportStatusDTO findById(Integer id) {
        ReportStatus entity = repository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportStatus", "statusId", id, ReportError.REPORT_STATUS_NOT_FOUND
                ));

        log.debug("Found ReportStatus with id: {}", id);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportStatusDTO findByCode(String statusCode) {
        ReportStatus entity = repository.findByStatusCode(statusCode)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportStatus", "statusCode", statusCode, ReportError.REPORT_STATUS_NOT_FOUND
                ));

        log.debug("Found ReportStatus with code: {}", statusCode);
        return mapper.toDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw exceptionFactory.createNotFoundException(
                    "ReportStatus", "statusId", id, ReportError.REPORT_STATUS_NOT_FOUND
            );
        }

        // ReportStatus entity = repository.findById(id).get();
        // if (entity.getReports() != null && !entity.getReports().isEmpty()) {
        //     throw exceptionFactory.createCustomException(
        //             List.of("reports"),
        //             List.of(entity.getReports().size()),
        //             ReportError.INVALID_REPORT_OPERATION
        //     );
        // }

        repository.deleteById(id);
        log.info("Deleted ReportStatus with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportStatusDTO> findAll() {
        List<ReportStatus> entities = repository.findAllByOrderByStatusOrderAsc();
        log.debug("Found {} ReportStatus records", entities.size());
        return mapper.toDTOList(entities);
    }

    @Override
    public ReportStatusDTO create(ReportStatusDTO dto) {
        if (repository.existsByStatusName(dto.getStatusName())) {
            throw exceptionFactory.createAlreadyExistsException(
                    "ReportStatus",
                    "statusName",
                    dto.getStatusName(),
                    ReportError.REPORT_STATUS_DUPLICATE
            );
        }

        if (repository.existsByStatusCode(dto.getStatusCode())) {
            throw exceptionFactory.createAlreadyExistsException(
                    "ReportStatus",
                    "statusCode",
                    dto.getStatusCode(),
                    ReportError.REPORT_STATUS_DUPLICATE
            );
        }

        ReportStatus entity = mapper.toEntity(dto);
        ReportStatus savedEntity = repository.save(entity);

        // log.info("Created ReportStatus: {} with code: {}",
        //         savedEntity.getStatusName(), savedEntity.getStatusCode());

        return mapper.toDTO(savedEntity);
    }

    @Override
    public ReportStatusDTO update(Integer id, ReportStatusDTO dto) {
        ReportStatus existingEntity = repository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportStatus", "statusId", id, ReportError.REPORT_STATUS_NOT_FOUND
                ));

        // if (!existingEntity.getStatusName().equals(dto.getStatusName())
        //         && repository.existsByStatusName(dto.getStatusName())) {
        //     throw exceptionFactory.createAlreadyExistsException(
        //             "ReportStatus",
        //             "statusName",
        //             dto.getStatusName(),
        //             ReportError.REPORT_STATUS_DUPLICATE
        //     );
        // }

        // if (!existingEntity.getStatusCode().equals(dto.getStatusCode())
        //         && repository.existsByStatusCode(dto.getStatusCode())) {
        //     throw exceptionFactory.createAlreadyExistsException(
        //             "ReportStatus",
        //             "statusCode",
        //             dto.getStatusCode(),
        //             ReportError.REPORT_STATUS_DUPLICATE
        //     );
        // }

        // existingEntity.setStatusName(dto.getStatusName());
        // existingEntity.setStatusCode(dto.getStatusCode());
        // existingEntity.setStatusOrder(dto.getStatusOrder());
        // existingEntity.setDescription(dto.getDescription());

        ReportStatus updatedEntity = repository.save(existingEntity);

        log.info("Updated ReportStatus with id: {}", id);

        return mapper.toDTO(updatedEntity);
    }
}