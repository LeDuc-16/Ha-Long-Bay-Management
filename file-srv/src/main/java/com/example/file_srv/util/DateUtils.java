package com.example.file_srv.util;

import com.example.file_srv.exception.AppException;
import com.example.file_srv.exception.ERROR_CODE;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    // Các hằng số định dạng ngày tháng
    public static final String SHORT_DATE_PATTERN = "dd/MM/yyyy";
    public static final String FULL_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    public static final String ISO_DATE_PATTERN = "yyyy-MM-dd"; // Giống của bạn

    /**
     * Chuyển đổi một chuỗi String thành đối tượng LocalDate.
     * @param dateString Chuỗi ngày tháng (ví dụ: "10/10/2025")
     * @param pattern Định dạng của chuỗi (ví dụ: "dd/MM/yyyy")
     * @return Đối tượng LocalDate
     */
    public static LocalDate stringToLocalDate(String dateString, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            log.error("Cannot parse date string: {}", dateString, e);
            // Bạn có thể ném ra một lỗi cụ thể hơn ở đây
            throw new RuntimeException("Invalid date format", e);
        }
    }

    /**
     * Chuyển đổi một đối tượng LocalDate thành chuỗi String.
     * @param date Đối tượng LocalDate
     * @param pattern Định dạng mong muốn (ví dụ: "dd/MM/yyyy")
     * @return Chuỗi ngày tháng đã được định dạng
     */
    public static String localDateToString(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    /**
     * Chuyển đổi một chuỗi String thành đối tượng LocalDateTime.
     * @param dateTimeString Chuỗi ngày giờ (ví dụ: "10/10/2025 17:30:00")
     * @param pattern Định dạng của chuỗi (ví dụ: "dd/MM/yyyy HH:mm:ss")
     * @return Đối tượng LocalDateTime
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeString, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            log.error("Cannot parse date-time string: {}", dateTimeString, e);
            throw new RuntimeException("Invalid date-time format", e);
        }
    }

    /**
     * Chuyển đổi một đối tượng LocalDateTime thành chuỗi String.
     * @param dateTime Đối tượng LocalDateTime
     * @param pattern Định dạng mong muốn (ví dụ: "dd/MM/yyyy HH:mm:ss")
     * @return Chuỗi ngày giờ đã được định dạng
     */
    public static String localDateTimeToString(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }
}