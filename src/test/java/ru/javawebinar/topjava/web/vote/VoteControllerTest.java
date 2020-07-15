package ru.javawebinar.topjava.web.vote;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.util.DateTimeUtil.getСhangeVoteTime;
import static ru.javawebinar.topjava.util.DateTimeUtil.setСhangeVoteTime;
import static ru.javawebinar.topjava.web.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.web.testdata.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.web.testdata.UserTestData.USER_ID;
import static ru.javawebinar.topjava.web.testdata.VoteTestData.*;

public class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    VoteController controller;

    @Test
    public void get() throws Exception {
        Vote vote = controller.get(VOTE1_ID, ADMIN_ID);
        VOTE_MATCHER.assertMatch(vote, VOTE1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getAllForUser() throws Exception {
        List<Vote> all = controller.getAllForUser(USER_ID);
        VOTE_MATCHER.assertMatch(all, VOTE3, VOTE5);
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
    public void delete() throws Exception {
        controller.delete(VOTE1_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> controller.get(VOTE1_ID, ADMIN_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void create() throws Exception {
        Vote created = controller.create(getNew(), ADMIN_ID);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(controller.get(newId, ADMIN_ID), newVote);
    }

    @Test
    public void duplicateVoteCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                controller.create(new Vote(null, of(2020, Month.JUNE, 30), RESTAURANT2_ID, USER_ID), USER_ID));
    }

    @Test
    public void update() throws Exception {
        LocalTime voteTime = getСhangeVoteTime();
        setСhangeVoteTime(LocalTime.now().plusMinutes(5));
        Vote updated = getUpdated();
        controller.update(updated, VOTE1_ID, ADMIN_ID);
        Vote expected = controller.get(VOTE1_ID, ADMIN_ID);
        setСhangeVoteTime(voteTime);
        VOTE_MATCHER.assertMatch(expected, getUpdated());
    }
}
