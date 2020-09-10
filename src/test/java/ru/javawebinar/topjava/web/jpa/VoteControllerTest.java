package ru.javawebinar.topjava.web.jpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.testdata.VoteTestData;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.*;
import static ru.javawebinar.topjava.testdata.VoteTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthorizedUserTest;

public class VoteControllerTest extends AbstractJpaControllerTest {

    @Autowired
    VoteController controller;

    @Test
    public void getById() throws Exception {
        setAuthorizedUserTest(ADMIN);
        Vote vote = controller.getById(VOTE1_ID);
        VOTE_MATCHER.assertMatch(vote, VOTE1);
    }

    @Test
    public void getNotFound() throws Exception {
        setAuthorizedUserTest(ADMIN);
        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND));
    }

    @Test
    public void getAll() throws Exception {
        List<Vote> all = controller.getAll();
        VOTE_MATCHER.assertMatch(all, allVotes());
    }

    @Test
    public void getAllForAuthUser() throws Exception {
        List<Vote> all = controller.getAllForAuthUser(USER_ID);
        VOTE_MATCHER.assertMatch(all, VOTE3, VOTE5, VOTE6);
    }

    @Test
    public void getAllForRestaurant() {
        List<Vote> all = controller.getAllForRestaurant(RESTAURANT1_ID);
        VOTE_MATCHER.assertMatch(all, allForRestaurant());
    }

    @Test
    public void getByDateForAuth() {
        setAuthorizedUserTest(ADMIN);
        Vote vote = controller.getByDateForAuth(of(2020, Month.JUNE, 28));
        VOTE_MATCHER.assertMatch(vote, VOTE1);
    }

    @Test
    public void getBetween() throws Exception {
        List<Vote> votes = controller.getBetween(
                of(2020, Month.JUNE, 25), of(2020, Month.JUNE, 29), USER_ID);
        VOTE_MATCHER.assertMatch(votes, VOTE3);
    }

    @Test
    public void delete() throws Exception {
        setAuthorizedUserTest(ADMIN);
        setСhangeVoteTime(TIME_TEST_IN);
        controller.delete(VOTE1_ID);
        assertThrows(NotFoundException.class, () -> controller.getById(VOTE1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        setAuthorizedUserTest(USER);
        setСhangeVoteTime(TIME_TEST_IN);
        assertThrows(NotFoundException.class, () -> controller.delete(VOTE1_ID));
    }

    @Test
    public void deleteOverTime() throws Exception {
        setAuthorizedUserTest(ADMIN);
        setСhangeVoteTime(TIME_TEST_OUT);
        assertThrows(NotFoundException.class, () -> controller.delete(VOTE1_ID));
    }

    @Test
    public void update() throws Exception {
        setAuthorizedUserTest(ADMIN);
        setСhangeVoteTime(TIME_TEST_IN);
        Vote updated = VoteTestData.getUpdated();
        controller.update(updated, VOTE1_ID, ADMIN_ID);
        Vote expected = controller.getById(VOTE1_ID);
        VOTE_MATCHER.assertMatch(expected, VoteTestData.getUpdated());
    }

    @Test
    public void updateOverTime() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        setСhangeVoteTime(TIME_TEST_OUT);
        assertThrows(NotFoundException.class, () -> controller.update(updated, VOTE1_ID, ADMIN_ID));
    }

    @Test
    public void updateNotOwn() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        setСhangeVoteTime(TIME_TEST_IN);
        assertThrows(NotFoundException.class, () -> controller.update(updated, VOTE1_ID, USER_ID));
    }

    @Test
    public void updateIllegalArgument() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        setСhangeVoteTime(TIME_TEST_IN);
        assertThrows(NotFoundException.class, () -> controller.update(updated, NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void create() throws Exception {
        setAuthorizedUserTest(ADMIN);
        Vote created = controller.create(VoteTestData.getNew(), ADMIN_ID);
        int newId = created.id();
        Vote newVote = VoteTestData.getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(controller.getById(newId), newVote);
    }

    @Test
    public void createIllegalArgument() throws Exception {
        assertThrows(NotFoundException.class, () ->
                controller.create(new Vote(VOTE1_ID, now(), RESTAURANT2_ID, ADMIN_ID), ADMIN_ID));
    }

    @Test
    public void createErrorData() throws Exception {
        assertThrows(NotFoundException.class, () ->
                controller.create(new Vote(null, now(), RESTAURANT2_ID, ADMIN_ID), NOT_FOUND));
    }

    @Test
    public void createDuplicateVote() throws Exception {
        Vote duplicate = new Vote(VOTE1);
        duplicate.setId(null);
        assertThrows(NotFoundException.class, () -> controller.create(duplicate, ADMIN_ID));
    }

    @Test
    public void createRepeatPerDay() throws Exception {
        Vote repeat = new Vote(VOTE1);
        repeat.setRestaurantId(RESTAURANT2_ID);
        assertThrows(NotFoundException.class, () -> controller.create(repeat, ADMIN_ID));
    }
}


