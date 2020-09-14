package ru.javawebinar.topjava.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.testdata.VoteTestData;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.json.JsonUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalDate;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.*;
import static ru.javawebinar.topjava.testdata.VoteTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthorizedUserTest;

class VoteRestControllerTest extends AbstractControllerTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteRestController controller;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE1));
    }

    @Test
    void getNotFound() throws Exception {
        setAuthorizedUserTest(ADMIN);
        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allVotes()));
    }

    @Test
    void getByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allForRestaurant()));
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "date/2020-06-30")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE5, VOTE4));
    }

    @Test
    void getByDateForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/date/" + DATE_TEST + "/users/" + USER_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        VOTE_MATCHER.assertMatch(controller.getByDateForUser(ADMIN_ID, DATE_TEST), VOTE7);
    }

    @Test
    void getAllForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/users/" + ADMIN_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allForAdmin()));
    }

    @Test
    void getBetweenForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "between/users/" + ADMIN_ID + "/start/2020-06-28/end/2020-06-29")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(between()));
    }

    @Test
    void update() throws Exception {
        DateTimeUtil.setСhangeVoteTime(TIME_TEST_IN);
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID + "/users/" + ADMIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(controller.getById(VOTE1_ID), updated);
    }

    @Test
    void updateOverTime() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        setСhangeVoteTime(TIME_TEST_OUT);
        assertThrows(NotFoundException.class, () -> controller.update(updated, VOTE1_ID, ADMIN_ID));
    }

    @Test
    void updateNotOwn() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        setСhangeVoteTime(TIME_TEST_IN);
        assertThrows(NotFoundException.class, () -> controller.update(updated, VOTE1_ID, USER_ID));
    }

    @Test
    void updateIllegalArgument() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        setСhangeVoteTime(TIME_TEST_IN);
        assertThrows(NotFoundException.class, () -> controller.update(updated, NOT_FOUND, ADMIN_ID));
    }

    @Test
    void create() throws Exception {
        setThisDay(LocalDate.now());
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(
                REST_URL + "restaurants/" + RESTAURANT2_ID + "/users/" + ADMIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(controller.getById(newId), newVote);
    }

    @org.junit.Test
    public void createErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.create(NOT_FOUND, ADMIN_ID));
        assertThrows(NotFoundException.class, () -> controller.create( RESTAURANT2_ID, NOT_FOUND));
    }

    @Test
    public void createRepeatPerDay() throws Exception {
        setThisDay(DATE_TEST);
        assertThrows(NotFoundException.class, () -> controller.create(RESTAURANT1_ID, ADMIN_ID));
    }

    @Test
    void delete() throws Exception {
        setСhangeVoteTime(TIME_TEST_IN);
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.getById(VOTE1_ID));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    void deleteNotOwn() throws Exception {
        setAuthorizedUserTest(USER);
        setСhangeVoteTime(TIME_TEST_IN);
        assertThrows(NotFoundException.class, () -> controller.delete(VOTE1_ID));
    }

    @Test
    void deleteOverTime() throws Exception {
        setAuthorizedUserTest(ADMIN);
        setСhangeVoteTime(TIME_TEST_OUT);
        assertThrows(NotFoundException.class, () -> controller.delete(VOTE1_ID));
    }
}
