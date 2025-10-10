package com.example.file_srv.service;

import com.example.file_srv.dto.request.FileUploadRequest;
import com.example.file_srv.dto.response.FileUploadResponse;

// Đây là "bản hợp đồng" định nghĩa các chức năng mà service sẽ cung cấp
public interface FileStorageService {

    // Chữ ký của phương thức upload file, nhận vào request và userId, trả về response
    FileUploadResponse uploadFile(FileUploadRequest request);
}