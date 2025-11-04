package com.c4.routy.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    // yyyy-MM-dd 형식으로
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 현재 시간 → 문자열
    public static String now() {
        return LocalDateTime.now().format(FORMATTER);
    }

    // LocalDateTime → 문자열
    public static String format(LocalDateTime time) {
        return time.format(FORMATTER);
    }

    // 문자열 → LocalDateTime
    public static LocalDateTime toLocalDateTime(String time) {
        return LocalDateTime.parse(time, FORMATTER);
    }
}
