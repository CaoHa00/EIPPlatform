package com.EIPplatform.service.fileStorage;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.model.dto.fileStorage.FileStorageRequest;
public interface FileStorageService {
    
    /**
     * Upload file với metadata (BusinessName, Sector)
     * @param file file cần upload
     * @param request metadata chứa businessName và sector
     * @return đường dẫn file đã upload (relative path)
     */
    String uploadFile(MultipartFile file, FileStorageRequest request);
    
    /**
     * Upload file lên storage (legacy - backward compatible)
     * @param file file cần upload
     * @param folder thư mục đích
     * @return đường dẫn file đã upload
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * Upload nhiều files với metadata
     */
    List<String> uploadFiles(List<MultipartFile> files, FileStorageRequest request);
    
    /**
     * Upload nhiều files (legacy)
     */
    List<String> uploadFiles(List<MultipartFile> files, String folder);

    /**
     * Download file với metadata
     * @param request metadata chứa businessName và sector
     * @param fileName tên file cần download
     * @return Resource để download
     */
    Resource downloadFile(FileStorageRequest request, String fileName);
    
    /**
     * Download file từ storage (legacy)
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
    
    /**
     * Liệt kê tất cả files trong một business/sector
     */
    List<String> listFiles(FileStorageRequest request);
}
