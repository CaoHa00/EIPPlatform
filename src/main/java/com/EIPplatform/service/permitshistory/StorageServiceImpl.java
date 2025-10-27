package com.EIPplatform.service.permitshistory;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.PermitError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Value("${app.storage.local.upload-dir:./uploads}")
    private String uploadDir;
    private Path rootLocation;
    ExceptionFactory exceptionFactory;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        log.info("Uploading file to folder: {}", folder);

        try {
            if (file.isEmpty()) {
                throw exceptionFactory.createValidationException("File", "empty", true, ValidationError.EMPTY_FILE);
            }
            initStorage();
            Path folderPath = rootLocation.resolve(folder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            String filename = generateUniqueFileName(file.getOriginalFilename());
            Path destinationFile = folderPath.resolve(filename).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(folderPath.toAbsolutePath())) {
                throw exceptionFactory.createCustomException("File", Arrays.asList("storagePath"), Arrays.asList("Cannot store file outside current directory"), ForbiddenError.FORBIDDEN);
            }

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            String filePath = folder + "/" + filename;
            log.info("File uploaded successfully: {}", filePath);
            return filePath;

        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String folder) {
        List<String> filePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            filePaths.add(uploadFile(file, folder));
        }
        return filePaths;
    }

    @Override
    public Resource downloadFile(String filePath) {
        log.info("Downloading file: {}", filePath);

        try {
            initStorage();
            Path file = rootLocation.resolve(filePath).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw exceptionFactory.createNotFoundException("File", "path", filePath, PermitError.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            log.error("Failed to download file", e);
            throw exceptionFactory.createNotFoundException("File", "path", filePath, PermitError.FILE_NOT_FOUND);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        log.info("Deleting file: {}", filePath);

        try {
            initStorage();
            Path file = rootLocation.resolve(filePath).normalize();
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

    @Override
    public boolean fileExists(String filePath) {
        initStorage();
        Path file = rootLocation.resolve(filePath).normalize();
        return Files.exists(file);
    }

    @Override
    public long getFileSize(String filePath) {
        try {
            initStorage();
            Path file = rootLocation.resolve(filePath).normalize();
            return Files.size(file);
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public String generateUniqueFileName(String originalFilename) {
        String filename = StringUtils.cleanPath(originalFilename);
        String extension = getFileExtension(filename);
        String nameWithoutExt = filename.substring(0, filename.lastIndexOf('.'));
        return nameWithoutExt + "_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8) + extension;
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

    private void initStorage() {
        if (rootLocation == null) {
            rootLocation = Paths.get(uploadDir);
            try {
                Files.createDirectories(rootLocation);
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize storage", e);
            }
        }
    }
}
