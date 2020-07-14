package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterUtil {
    public static AtomicInteger COUNTER = new AtomicInteger(0);

    public static Integer id(){
        return COUNTER.incrementAndGet();
    }
}
