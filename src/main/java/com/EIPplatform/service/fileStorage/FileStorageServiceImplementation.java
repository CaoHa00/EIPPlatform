package com.EIPplatform.service.fileStorage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.PermitError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.model.dto.fileStorage.FileStorageRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImplementation implements FileStorageService {

    @Value("${app.storage.local.upload-dir:/app/uploads}")
    private String uploadDir;

    private Path rootLocation;

    private final ExceptionFactory exceptionFactory;

    // ==================== UPLOAD METHODS ====================
    /**
     * Upload file với metadata (recommended method)
     */
    @Override
    public String uploadFile(MultipartFile file, FileStorageRequest request) {
        log.info("Uploading file with metadata - Business: {}, Sector: {}",
                request.getBusinessName(), request.getSector());

        // Validate request
        request.validateFields();

        // Get folder path từ metadata
        String folder = request.toFolderPath();

        // Gọi method upload cũ
        return uploadFile(file, folder);
    }

    /**
     * Upload file (legacy method)
     */
    @Override
    public String uploadFile(MultipartFile file, String folder) {
        log.info("Uploading file to folder: {}", folder);

        try {
            // Validate file
            if (file.isEmpty()) {
                throw exceptionFactory.createValidationException(
                        "File", "empty", true, ValidationError.EMPTY_FILE);
            }

            // Validate folder path
            validateFolderPath(folder);

            initStorage();

            // Tạo folder nếu chưa tồn tại
            Path folderPath = rootLocation.resolve(folder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            // Generate unique filename
            String filename = generateUniqueFileName(file.getOriginalFilename());
            Path destinationFile = folderPath.resolve(filename).normalize().toAbsolutePath();

            // Security check: đảm bảo file nằm trong folder đích
            if (!destinationFile.getParent().equals(folderPath.toAbsolutePath())) {
                throw exceptionFactory.createCustomException(
                        "File",
                        Arrays.asList("storagePath"),
                        Arrays.asList("Cannot store file outside current directory"),
                        ForbiddenError.FORBIDDEN);
            }

            // Additional security check
            if (!destinationFile.startsWith(rootLocation.toAbsolutePath())) {
                throw exceptionFactory.createCustomException(
                        "File",
                        Arrays.asList("storagePath"),
                        Arrays.asList("Cannot store file outside root directory"),
                        ForbiddenError.FORBIDDEN);
            }

            // Copy file
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            String filePath = folder + "/" + filename;
            log.info("File uploaded successfully: {}", filePath);
            return filePath;

        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    /**
     * Store file to a specific directory with given filename (new method for
     * explicit control)
     * 
     * @param directory thư mục đích (relative to root)
     * @param fileName  tên file (đã unique hoặc custom)
     * @param file      MultipartFile cần store
     * @return đường dẫn file đã store (relative path)
     */
    @Override
    public String storeFile(String directory, String fileName, MultipartFile file) {
        log.info("Storing file to directory: {}, filename: {}", directory, fileName);

        try {
            // Validate file
            if (file.isEmpty()) {
                throw exceptionFactory.createValidationException(
                        "File", "empty", true, ValidationError.EMPTY_FILE);
            }

            // Validate directory and filename
            validateFolderPath(directory);
            validateFileName(fileName);

            initStorage();

            // Tạo directory nếu chưa tồn tại
            Path dirPath = rootLocation.resolve(directory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Build destination path
            Path destinationFile = dirPath.resolve(fileName).normalize().toAbsolutePath();

            // Security checks
            if (!destinationFile.getParent().equals(dirPath.toAbsolutePath())) {
                throw exceptionFactory.createCustomException(
                        "File",
                        Arrays.asList("storagePath"),
                        Arrays.asList("Cannot store file outside current directory"),
                        ForbiddenError.FORBIDDEN);
            }

            if (!destinationFile.startsWith(rootLocation.toAbsolutePath())) {
                throw exceptionFactory.createCustomException(
                        "File",
                        Arrays.asList("storagePath"),
                        Arrays.asList("Cannot store file outside root directory"),
                        ForbiddenError.FORBIDDEN);
            }

            // Copy file
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            String filePath = directory + "/" + fileName;
            log.info("File stored successfully: {}", filePath);
            return filePath;

        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    /**
     * Upload nhiều files với metadata
     */
    @Override
    public List<String> uploadFiles(List<MultipartFile> files, FileStorageRequest request) {
        log.info("Uploading {} files with metadata - Business: {}, Sector: {}",
                files.size(), request.getBusinessName(), request.getSector());

        List<String> filePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            filePaths.add(uploadFile(file, request));
        }
        return filePaths;
    }

    /**
     * Upload nhiều files (legacy)
     */
    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String folder) {
        List<String> filePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            filePaths.add(uploadFile(file, folder));
        }
        return filePaths;
    }

    // ==================== DOWNLOAD METHODS ====================
    /**
     * Download file với metadata (recommended method)
     */
    @Override
    public Resource downloadFile(FileStorageRequest request, String fileName) {
        log.info("Downloading file with metadata - Business: {}, Sector: {}, File: {}",
                request.getBusinessName(), request.getSector(), fileName);

        // Validate
        request.validateFields();
        validateFileName(fileName);

        // Build full path
        String filePath = request.toFullPath(fileName);

        // Gọi method download cũ
        return downloadFile(filePath);
    }

    /**
     * Download file (legacy method)
     */
    @Override
    public Resource downloadFile(String filePath) {
        System.out.println("Downloading file: " + filePath);

        try {
            initStorage();
            Path file = rootLocation.resolve(filePath).normalize();

            // Security check: đảm bảo file nằm trong root directory
            if (!file.startsWith(rootLocation.toAbsolutePath())) {
                throw exceptionFactory.createNotFoundException(
                        "File", "path", filePath, PermitError.FILE_NOT_FOUND);
            }

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw exceptionFactory.createNotFoundException(
                        "File", "path", filePath, PermitError.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            log.error("Failed to download file", e);
            throw exceptionFactory.createNotFoundException(
                    "File", "path", filePath, PermitError.FILE_NOT_FOUND);
        }
    }

    // ==================== DELETE METHODS ====================
    @Override
    public void deleteFile(String filePath) {
        log.info("Deleting file: {}", filePath);

        try {
            initStorage();
            Path file = rootLocation.resolve(filePath).normalize();

            // Security check
            if (!file.startsWith(rootLocation.toAbsolutePath())) {
                log.warn("Attempted to delete file outside root directory: {}", filePath);
                return;
            }

            Files.deleteIfExists(file);
            log.info("File deleted successfully: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to delete file", e);
        }
    }

    @Override
    public void deleteFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            deleteFile(filePath);
        }
    }

    // ==================== UTILITY METHODS ====================
    @Override
    public boolean fileExists(String filePath) {
        initStorage();
        Path file = rootLocation.resolve(filePath).normalize();

        // Security check
        if (!file.startsWith(rootLocation.toAbsolutePath())) {
            return false;
        }

        return Files.exists(file);
    }

    @Override
    public long getFileSize(String filePath) {
        try {
            initStorage();
            Path file = rootLocation.resolve(filePath).normalize();

            // Security check
            if (!file.startsWith(rootLocation.toAbsolutePath())) {
                return 0;
            }

            return Files.size(file);
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public String generateUniqueFileName(String originalFilename) {
        String filename = StringUtils.cleanPath(originalFilename);

        // Handle files without extension
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return filename + "_" + System.currentTimeMillis() + "_"
                    + UUID.randomUUID().toString().substring(0, 8);
        }

        String extension = getFileExtension(filename);
        String nameWithoutExt = filename.substring(0, lastDotIndex);

        return nameWithoutExt + "_" + System.currentTimeMillis() + "_"
                + UUID.randomUUID().toString().substring(0, 8) + extension;
    }

    @Override
    public String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }

    /**
     * Liệt kê tất cả files trong một business/sector
     */
    @Override
    public List<String> listFiles(FileStorageRequest request) {
        log.info("Listing files - Business: {}, Sector: {}",
                request.getBusinessName(), request.getSector());

        try {
            initStorage();
            request.validateFields();

            Path folderPath = rootLocation.resolve(request.toFolderPath());

            if (!Files.exists(folderPath)) {
                log.info("Folder does not exist: {}", folderPath);
                return Collections.emptyList();
            }

            return Files.list(folderPath)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Failed to list files", e);
            return Collections.emptyList();
        }
    }

    // ==================== PRIVATE METHODS ====================
    /**
     * Khởi tạo storage directory
     */
    private void initStorage() {
        if (rootLocation == null) {
            rootLocation = Paths.get(uploadDir);
            try {
                Files.createDirectories(rootLocation);
                log.info("Storage initialized at: {}", rootLocation.toAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize storage", e);
            }
        }
    }

    /**
     * Validate folder path để tránh path traversal
     */
    private void validateFolderPath(String folder) {
        if (folder == null || folder.trim().isEmpty()) {
            throw new IllegalArgumentException("Folder path cannot be null or empty");
        }

        // Kiểm tra path traversal
        if (folder.contains("..")) {
            throw new SecurityException("Invalid folder path: contains '..'");
        }

        // Kiểm tra null byte injection
        if (folder.contains("\0")) {
            throw new SecurityException("Invalid folder path: contains null byte");
        }
    }

    /**
     * Validate file name để tránh path traversal
     */
    private void validateFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        // Kiểm tra path traversal
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new SecurityException("Invalid file name: contains path separators");
        }

        // Kiểm tra null byte injection
        if (fileName.contains("\0")) {
            throw new SecurityException("Invalid file name: contains null byte");
        }

        // Kiểm tra độ dài
        if (fileName.length() > 255) {
            throw new IllegalArgumentException("File name too long (max 255 characters)");
        }
    }
}