package com.example.file_srv.controller;

import com.example.file_srv.dto.ApiResponse;
import com.example.file_srv.dto.request.FileUploadRequest;
import com.example.file_srv.dto.response.FileUploadResponse;
import com.example.file_srv.service.FileStorageServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileStorageController {
    FileStorageServiceImpl fileStorageServiceimpl;

    @PostMapping("/upload")
    public ApiResponse<FileUploadResponse> uploadFile(FileUploadRequest file) {
        return ApiResponse.<FileUploadResponse>builder()
                .data(fileStorageServiceimpl.uploadFile(file))
                .build();
    }

}