package ru.javawebinar.topjava.web.rest.admin;

import org.junit.jupiter.api.Test;
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

import java.util.Date;

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

class VoteRestControllerTest extends AbstractControllerTest {
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
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
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
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "date")
                .param("localDate", "2020-06-30")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE4, VOTE5));
    }

    @Test
    void getByDateForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/users/" + USER_ID)
                .param("localDate", "2020-06-30")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VOTE_MATCHER.contentJson(VOTE5));
    }

    @Test
    void getAllForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/users")
                .param("id", String.valueOf(ADMIN_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allForAdmin()));
    }

    @Test
    void getBetweenForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "between")
                .param("startDate", "2020-06-28")
                .param("endDate", "2020-06-29")
                .param("userId", String.valueOf(ADMIN_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(between()));
    }

    @Test
    void update() throws Exception {
        setСhangeVoteTime(TIME_TEST_IN);
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .param("voteId", String.valueOf(VOTE1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateOverTime() throws Exception {
        DateTimeUtil.setСhangeVoteTime(TIME_TEST_OUT);
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .param("voteId", String.valueOf(VOTE1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void updateNotOwn() throws Exception {
        DateTimeUtil.setСhangeVoteTime(TIME_TEST_IN);
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .param("voteId", String.valueOf(VOTE3_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateIllegalArgument() throws Exception {
        DateTimeUtil.setСhangeVoteTime(TIME_TEST_IN);
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .param("voteId", String.valueOf(NOT_FOUND))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    void create() throws Exception {
        setThisDay(new Date());
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        int newId = created.id();
        newVote.setId(newId);
        newVote.setLocalDate(created.getLocalDate());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(controller.getById(newId), newVote);
    }

    @Test
    void createRepeatPerDay() throws Exception {
        setThisDay(DATE_TEST);
        Vote newVote = VoteTestData.getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
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
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteNotOwn() throws Exception {
        setСhangeVoteTime(TIME_TEST_IN);
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteOverTime() throws Exception {
        setСhangeVoteTime(TIME_TEST_OUT);
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }
}
