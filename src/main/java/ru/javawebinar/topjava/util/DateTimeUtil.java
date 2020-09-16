package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {

    public static final LocalDate DATE_TEST = LocalDate.of(2020,07,30);
    public static final LocalTime TIME_TEST_IN = LocalTime.now().plusMinutes(10);
    public static final LocalTime TIME_TEST_OUT = LocalTime.now().minusMinutes(10);

    public static LocalTime сhangeVoteTime = LocalTime.of(11, 0);

    public static LocalDate thisDay = LocalDate.now();

    public static void setСhangeVoteTime(LocalTime сhangeVoteTime) { DateTimeUtil.сhangeVoteTime = сhangeVoteTime; }

    public static void setThisDay(LocalDate thisDay) { DateTimeUtil.thisDay = thisDay; }

    private DateTimeUtil() {
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
