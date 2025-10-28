package com.EIPplatform.service.permits;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface StorageService {

    /**
     * Upload file lên storage
     * @param file file cần upload
     * @param folder thư mục đích
     * @return đường dẫn file đã upload
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * Upload nhiều files
     */
    List<String> uploadFiles(List<MultipartFile> files, String folder);

    /**
     * Download file từ storage
     */
    Resource downloadFile(String filePath);

    /**
     * Xóa file khỏi storage
     */
    void deleteFile(String filePath);

    /**
     * Xóa nhiều files
     */
    void deleteFiles(List<String> filePaths);

    /**
     * Kiểm tra file có tồn tại không
     */
    boolean fileExists(String filePath);

    /**
     * Lấy kích thước file
     */
    long getFileSize(String filePath);

    /**
     * Generate tên file unique
     */
    String generateUniqueFileName(String originalFilename);

    /**
     * Lấy file extension
     */
    String getFileExtension(String filename);
}