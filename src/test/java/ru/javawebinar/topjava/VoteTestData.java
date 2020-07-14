package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Vote;

import java.time.Month;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static ru.javawebinar.topjava.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final int VOTE1_ID = START_SEQ + 15;
    public static final int VOTE2_ID = VOTE1_ID + 1;
    public static final int NOT_FOUND = 100;

    public static final Vote VOTE1 = new Vote(VOTE1_ID, of(2020, Month.JUNE, 28), RESTAURANT1_ID, USER_ID);
    public static final Vote VOTE2 = new Vote(VOTE1_ID + 1, of(2020, Month.JUNE, 29), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote VOTE3 = new Vote(VOTE1_ID + 2, of(2020, Month.JUNE, 29), RESTAURANT1_ID, USER_ID);
    public static final Vote VOTE4 = new Vote(VOTE1_ID + 3, of(2020, Month.JUNE, 30), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote VOTE5 = new Vote(VOTE1_ID + 4, of(2020, Month.JUNE, 30), RESTAURANT2_ID, USER_ID);

    public static Vote getNew() {
        return new Vote(null, now(), RESTAURANT2_ID, ADMIN_ID);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE1_ID, of(2020, Month.JUNE, 28), RESTAURANT2_ID, USER_ID);
    }

}
