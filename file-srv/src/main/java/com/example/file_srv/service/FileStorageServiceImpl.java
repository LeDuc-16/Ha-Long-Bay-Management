package com.example.file_srv.service;

import com.example.file_srv.dto.request.FileUploadRequest;
import com.example.file_srv.dto.response.FileUploadResponse;
import com.example.file_srv.exception.AppException;
import com.example.file_srv.exception.ERROR_CODE;
import com.example.file_srv.util.DateUtils; // Đã import
import com.example.file_srv.util.FileUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate; // Đã import
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public FileStorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public FileUploadResponse uploadFile(FileUploadRequest request) {
        MultipartFile file = request.getFile();

        if (file == null || file.isEmpty()) {
            throw new AppException(ERROR_CODE.FILE_CANNOT_BE_EMPTY);
        }

        FileUtils.validateFileType(file);

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 1. Lấy đường dẫn ngày tháng năm từ DateUtils
            String datePath = DateUtils.localDateToString(LocalDate.now(), "yyyy/MM/dd");

            // 2. Tạo tên file duy nhất (vẫn giữ UUID để tránh trùng lặp)
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID().toString() + "-" + originalFilename;

            // 3. Kết hợp đường dẫn và tên file duy nhất
            String objectName = datePath + "/" + uniqueFilename;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName) // Sử dụng tên object mới có chứa đường dẫn
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            log.info("File uploaded successfully, organized under path: {}", objectName);

            return FileUploadResponse.builder()
                    .objectName(objectName)
                    .originalFilename(originalFilename)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();

        } catch (Exception e) {
            log.error("Error occurred while uploading file to MinIO: {}", e.getMessage());
            throw new AppException(ERROR_CODE.FILE_UPLOAD_FAILED);
        }
    }
}