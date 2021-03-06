package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.Date;

public class DateTimeUtil {
    public static final Date DATE_TEST = toDate(2020, 7, 30);

    public static final LocalTime TIME_TEST_IN = LocalTime.now().plusMinutes(10);
    public static final LocalTime TIME_TEST_OUT = LocalTime.now().minusMinutes(10);

    public static LocalTime сhangeVoteTime = LocalTime.of(11, 0);

    public static Date thisDay = clearTime(new Date());

    public static void setСhangeVoteTime(LocalTime сhangeVoteTime) {
        DateTimeUtil.сhangeVoteTime = сhangeVoteTime;
    }

    public static void setThisDay(Date thisDay) {
        DateTimeUtil.thisDay = thisDay;
    }

    public static @Nullable
    LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static @Nullable Date toDate(@Nullable int year, @Nullable int month, @Nullable int day) {
        Date date = new Date(year - 1900, month - 1, day);
        return clearTime(date);
    }

    public static Date clearTime(Date date) {
        date.setSeconds(0);
        date.setMinutes(0);
        date.setHours(0);
        return date;
    }
}
