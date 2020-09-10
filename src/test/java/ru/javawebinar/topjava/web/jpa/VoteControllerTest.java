package ru.javawebinar.topjava.web.jpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.testdata.VoteTestData;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.*;
import static ru.javawebinar.topjava.testdata.VoteTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.TIME_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setСhangeVoteTime;
import static ru.javawebinar.topjava.web.SecurityUtil.setTestAuthorizedUser;

public class VoteControllerTest extends AbstractJpaControllerTest {

    @Autowired
    VoteController controller;

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
    public void getAllForRestaurant() {
        List<Vote> all = controller.getAllForRestaurant(RESTAURANT1_ID);
        VOTE_MATCHER.assertMatch(all, allForRestaurant());
    }

    @Test
    public void getByDateForAuth() {
        setTestAuthorizedUser(ADMIN);
        Vote vote = controller.getByDateForAuth(of(2020, Month.JUNE, 28));
        VOTE_MATCHER.assertMatch(vote, VOTE1);
    }
    @Test
    public void delete() throws Exception {
        setTestAuthorizedUser(ADMIN);
        setСhangeVoteTime(TIME_TEST);
        controller.delete(VOTE1_ID);
        assertThrows(NotFoundException.class, () -> controller.getById(VOTE1_ID));
    }

    @Test
    public void create() throws Exception {
        setTestAuthorizedUser(ADMIN);
        Vote created = controller.create(VoteTestData.getNew(), ADMIN_ID);
        int newId = created.id();
        Vote newVote = VoteTestData.getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(controller.getById(newId), newVote);
    }

    @Test
    public void update() throws Exception {
        setTestAuthorizedUser(ADMIN);
        setСhangeVoteTime(TIME_TEST);
        Vote updated = VoteTestData.getUpdated();
        controller.update(updated, VOTE1_ID, ADMIN_ID);
        Vote expected = controller.getById(VOTE1_ID);
        VOTE_MATCHER.assertMatch(expected, VoteTestData.getUpdated());
    }

    @Test
    public void getById() throws Exception {
        setTestAuthorizedUser(ADMIN);
        Vote vote = controller.getById(VOTE1_ID);
        VOTE_MATCHER.assertMatch(vote, VOTE1);
    }
}




