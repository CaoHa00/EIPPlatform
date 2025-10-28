package com.EIPplatform.service.permits;

import com.EIPplatform.model.dto.permitshistory.CreatePermitRequest;
import com.EIPplatform.model.dto.permitshistory.EnvPermitDTO;
import com.EIPplatform.model.dto.permitshistory.PermitFilterRequest;
import com.EIPplatform.model.dto.permitshistory.UpdatePermitRequest;
import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PermitService {
    /**
     * Tạo giấy phép môi trường mới
     */
    EnvPermitDTO createPermit(UUID userId, CreatePermitRequest request, MultipartFile file);

    /**
     * Cập nhật thông tin giấy phép
     */
    EnvPermitDTO updatePermit(UUID userId, Long permitId, UpdatePermitRequest request);

    /**
     * Upload file giấy phép
     */
    EnvPermitDTO uploadPermitFile(UUID userId, Long permitId, MultipartFile file);

    /**
     * Lấy danh sách giấy phép của doanh nghiệp
     */
    List<EnvPermitDTO> getPermitsByUser(UUID userId, PermitFilterRequest filter);

    /**
     * Lấy chi tiết giấy phép
     */
    EnvPermitDTO getPermitById(UUID userId, Long permitId);

    /**
     * Vô hiệu hóa giấy phép
     */
    void deactivatePermit(UUID userId, Long permitId);

    /**
     * Kích hoạt giấy phép
     */
    void activatePermit(UUID userId, Long permitId);

    /**
     * Xóa giấy phép
     */
    void deletePermit(UUID userId, Long permitId);

    /**
     * Download file giấy phép
     */
    Resource downloadPermitFile(UUID userId, Long permitId);

    /**
     * Kiểm tra giấy phép sắp hết hạn
     */
    List<EnvPermitDTO> getExpiringPermits(UUID userId, int daysThreshold);

    /**
     * Lấy tất cả giấy phép của user
     */
    List<EnvPermitDTO> getAllPermitsByUser(UUID userId);
}