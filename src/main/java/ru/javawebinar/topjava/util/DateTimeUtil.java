package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalTime сhangeVoteTime = LocalTime.of(21, 0);

    /*public static LocalDate thisDay = LocalDate.now();*/
    public static LocalDate thisDay = LocalDate.of(2020,07,30);

    public static LocalTime getСhangeVoteTime() { return сhangeVoteTime; }

    public static void setСhangeVoteTime(LocalTime сhangeVoteTime) { DateTimeUtil.сhangeVoteTime = сhangeVoteTime; }

    public static LocalDate getThisDay() { return thisDay; }

    public static void setThisDay(LocalDate thisDay) { DateTimeUtil.thisDay = thisDay; }

    private DateTimeUtil() {
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static String toString(LocalDate ld) {
        return ld == null ? "" : ld.format(DATE_FORMATTER);
    }

    public static @Nullable LocalDateTime parseLocalDateTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDateTime.parse(str);
    }

    public static @Nullable LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static @Nullable LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }
}
