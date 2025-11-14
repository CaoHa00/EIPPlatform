package com.EIPplatform.controller.fileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.model.dto.fileStorage.FileStorageRequest;
import com.EIPplatform.service.fileStorage.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileStorageService fileStorageService;

    /**
     * Upload single file
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("businessName") String businessName,
            @RequestParam("sector") String sector) {

        log.info("Upload request - Business: {}, Sector: {}, File: {}",
                businessName, sector, file.getOriginalFilename());

        FileStorageRequest request = FileStorageRequest.builder()
                .businessName(businessName)
                .sector(sector)
                .build();

        String filePath = fileStorageService.uploadFile(file, request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("filePath", filePath);
        response.put("fileName", file.getOriginalFilename());
        response.put("fileSize", file.getSize());
        response.put("businessName", businessName);
        response.put("sector", sector);

        return ResponseEntity.ok(response);
    }

    /**
     * Upload multiple files
     */
    @PostMapping("/upload/multiple")
    public ResponseEntity<Map<String, Object>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("businessName") String businessName,
            @RequestParam("sector") String sector) {

        log.info("Upload multiple files - Business: {}, Sector: {}, Count: {}",
                businessName, sector, files.size());

        FileStorageRequest request = FileStorageRequest.builder()
                .businessName(businessName)
                .sector(sector)
                .build();

        List<String> filePaths = fileStorageService.uploadFiles(files, request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("filePaths", filePaths);
        response.put("count", filePaths.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Download file
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam("businessName") String businessName,
            @RequestParam("sector") String sector,
            @RequestParam("fileName") String fileName) {

        log.info("Download request - Business: {}, Sector: {}, File: {}",
                businessName, sector, fileName);

        FileStorageRequest request = FileStorageRequest.builder()
                .businessName(businessName)
                .sector(sector)
                .build();

        Resource resource = fileStorageService.downloadFile(request, fileName);

        // Detect content type
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(Paths.get(fileName));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (IOException e) {
            log.warn("Could not determine file type for: {}", fileName);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    /**
     * List all files in a business/sector
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listFiles(
            @RequestParam("businessName") String businessName,
            @RequestParam("sector") String sector) {

        log.info("List files - Business: {}, Sector: {}", businessName, sector);

        FileStorageRequest request = FileStorageRequest.builder()
                .businessName(businessName)
                .sector(sector)
                .build();

        List<String> files = fileStorageService.listFiles(request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("businessName", businessName);
        response.put("sector", sector);
        response.put("files", files);
        response.put("count", files.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Delete file
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteFile(
            @RequestParam("businessName") String businessName,
            @RequestParam("sector") String sector,
            @RequestParam("fileName") String fileName) {

        log.info("Delete request - Business: {}, Sector: {}, File: {}",
                businessName, sector, fileName);

        FileStorageRequest request = FileStorageRequest.builder()
                .businessName(businessName)
                .sector(sector)
                .build();

        String filePath = request.toFullPath(fileName);
        fileStorageService.deleteFile(filePath);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "File deleted successfully");
        response.put("filePath", filePath);

        return ResponseEntity.ok(response);
    }

    /**
     * Check if file exists
     */
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> fileExists(
            @RequestParam("businessName") String businessName,
            @RequestParam("sector") String sector,
            @RequestParam("fileName") String fileName) {

        FileStorageRequest request = FileStorageRequest.builder()
                .businessName(businessName)
                .sector(sector)
                .build();

        String filePath = request.toFullPath(fileName);
        boolean exists = fileStorageService.fileExists(filePath);

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("filePath", filePath);

        return ResponseEntity.ok(response);
    }
}