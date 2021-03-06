package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Vote;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.DateTimeUtil.toDate;

public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsComparator(Vote.class,"date");
    public static final int VOTE1_ID = START_SEQ + 15;
    public static final int VOTE3_ID = VOTE1_ID + 2;
    public static final Vote VOTE1 = new Vote(VOTE1_ID,  toDate(2020, 06, 28), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote VOTE2 = new Vote(VOTE1_ID + 1, toDate(2020, 06, 29), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote VOTE3 = new Vote(VOTE1_ID + 2,  toDate(2020, 06, 29), RESTAURANT1_ID, USER_ID);
    public static final Vote VOTE4 = new Vote(VOTE1_ID + 3,  toDate(2020, 06, 30), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote VOTE5 = new Vote(VOTE1_ID + 4,  toDate(2020, 06, 30), RESTAURANT2_ID, USER_ID);
    public static final Vote VOTE6 = new Vote(VOTE1_ID + 5,  toDate(2020, 07, 29), RESTAURANT2_ID, USER_ID);
    public static final Vote VOTE7 = new Vote(VOTE1_ID + 6,  toDate(2020, 07, 30), RESTAURANT1_ID, ADMIN_ID);

    public static Vote getNew() {
        return new Vote(null,  new Date(), RESTAURANT2_ID, ADMIN_ID);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE1_ID,  toDate(2020, 06, 28), RESTAURANT2_ID, ADMIN_ID);
    }

    public static List<Vote> allVotes(){
        return Arrays.asList(VOTE1, VOTE2, VOTE3, VOTE4, VOTE5, VOTE6, VOTE7);
    }

    public static List<Vote> allForRestaurant(){
        return Arrays.asList(VOTE1, VOTE2, VOTE3, VOTE4, VOTE7);
    }

    public static List<Vote> allForAuth(){
        return Arrays.asList(VOTE1, VOTE2, VOTE4, VOTE7);
    }

    public static List<Vote> allForAdmin(){
        return Arrays.asList(VOTE1, VOTE2, VOTE4, VOTE7);
    }

    public static List<Vote> between(){
        return Arrays.asList(VOTE1, VOTE2);
    }
}
