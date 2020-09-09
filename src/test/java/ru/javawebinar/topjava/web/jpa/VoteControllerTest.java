package ru.javawebinar.topjava.web.jpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.USER_ID;
import static ru.javawebinar.topjava.testdata.VoteTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.TIME_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setСhangeVoteTime;

public class VoteControllerTest extends AbstractJpaControllerTest {

    @Autowired
    VoteController controller;

    @Test
    public void getById() throws Exception {
        Vote vote = controller.getById(VOTE1_ID);
        VOTE_MATCHER.assertMatch(vote, VOTE1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND));
    }

    @Test
    public void getAllForAuthUser() throws Exception {
        List<Vote> all = controller.getAllForAuthUser(USER_ID);
        VOTE_MATCHER.assertMatch(all, VOTE3, VOTE5, VOTE6);
    }

    @Test
    public void getBetween() throws Exception {
        List<Vote> votes = controller.getBetween(
                of(2020, Month.JUNE, 25), of(2020, Month.JUNE, 29), USER_ID);
        VOTE_MATCHER.assertMatch(votes, VOTE3);
    }

    @Test
    public void getAll() throws Exception {
        List<Vote> all = controller.getAll();
        VOTE_MATCHER.assertMatch(all, allVotes());
    }

    @Test
    public void getByDateForAuth() {
        Vote vote = controller.getByDateForAuth(of(2020, Month.JUNE, 28));
        VOTE_MATCHER.assertMatch(vote, VOTE1);
    }

    @Test
    public void getAllForRestaurant() {
        List<Vote> all = controller.getAllForRestaurant(RESTAURANT1_ID);
        VOTE_MATCHER.assertMatch(all, allForRestaurant());
    }

    @Test
    public void delete() throws Exception {
        controller.delete(VOTE1_ID);
        assertThrows(NotFoundException.class, () -> controller.getById(VOTE1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void create() throws Exception {
        Vote created = controller.create(getNew(), ADMIN_ID);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(controller.getById(newId), newVote);
    }

    @Test
    public void duplicateVoteCreate() throws Exception {
        assertThrows(NotFoundException.class, () ->
                controller.create(new Vote(null, of(2020, Month.JUNE, 30), RESTAURANT2_ID, ADMIN_ID), ADMIN_ID));
    }

    @Test
    public void update() throws Exception {
        setСhangeVoteTime(TIME_TEST);
        Vote updated = getUpdated();
        controller.update(updated, VOTE1_ID, ADMIN_ID);
        Vote expected = controller.getById(VOTE1_ID);
        VOTE_MATCHER.assertMatch(expected, getUpdated());
    }
}
