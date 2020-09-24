package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.Date;

public class DateTimeUtil {
    private static Logger log = LoggerFactory.getLogger(DateTimeUtil.class);
    public static final Date DATE_TEST =  toDate(2020,7,30);

    public static final LocalTime TIME_TEST_IN = LocalTime.now().plusMinutes(10);
    public static final LocalTime TIME_TEST_OUT = LocalTime.now().minusMinutes(10);

    public static LocalTime сhangeVoteTime = LocalTime.of(11, 0);

    public static Date thisDay = new Date();

    public static void setСhangeVoteTime(LocalTime сhangeVoteTime) { DateTimeUtil.сhangeVoteTime = сhangeVoteTime; }

    public static void setThisDay(Date thisDay) { DateTimeUtil.thisDay = thisDay; }

    private DateTimeUtil() {
    }

    public static @Nullable LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static @Nullable Date toDate(@Nullable int year, @Nullable int month, @Nullable int day) {
        return new Date(year - 1900, month - 1, day);
    }
}
