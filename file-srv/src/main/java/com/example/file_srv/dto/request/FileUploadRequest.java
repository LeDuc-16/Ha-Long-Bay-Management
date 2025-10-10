package com.example.file_srv.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadRequest {

    @NotNull(message = "File cannot be null or empty")
    private MultipartFile file;
}