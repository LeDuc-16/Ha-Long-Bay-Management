package com.example.file_srv.util;

import com.example.file_srv.exception.AppException;
import com.example.file_srv.exception.ERROR_CODE;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {
    private static final Tika tika = new Tika();

    // Mở rộng danh sách các MIME Type được cho phép
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
            // Images
            "image/jpeg",       // .jpg, .jpeg
            "image/png",        // .png

            // Microsoft Office
            "application/msword", // .doc (Word 97-2003)
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
            "application/x-tika-msoffice",

            // PDF
            "application/pdf",  // .pdf

            // AutoCAD (Lưu ý: '.dxg' có thể bạn gõ nhầm từ '.dwg' hoặc '.dxf')
            "image/vnd.dwg",    // .dwg
            "image/vnd.dxf",    // .dxf

            // File .tab (Lưu ý: Đây là trường hợp không rõ ràng)
            "text/tab-separated-values", // Nếu là file text chứa dữ liệu phân cách bằng tab
            "text/plain"                 // Tika có thể nhận diện là text/plain
    );

    public static void validateFileType(MultipartFile file) {
        try {
            String detectedType = tika.detect(file.getInputStream());
            log.info("Detected MIME type for file '{}': {}", file.getOriginalFilename(), detectedType);

            if (!ALLOWED_FILE_TYPES.contains(detectedType)) {
                throw new AppException(ERROR_CODE.INVALID_FILE_TYPE);
            }
        } catch (IOException e) {
            log.error("Failed to read file for type detection", e);
            throw new AppException(ERROR_CODE.FILE_UPLOAD_FAILED);
        }
    }
}