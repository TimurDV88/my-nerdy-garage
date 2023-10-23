package com.mynerdygarage.util;

import com.mynerdygarage.error.exception.IncorrectRequestException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate stringToDate(String dateString) {

        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestException("- Wrong date format");
        }
    }

    public static LocalDateTime stringToDateTime(String dateString) {

        try {
            return LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestException("- Wrong date and time format");
        }
    }
}
