package ru.javawebinar.topjava.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.testdata.VoteTestData;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.json.JsonUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static java.lang.String.valueOf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.testdata.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.testdata.VoteTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.*;

class ProfileVoteRestControllerTest extends AbstractControllerTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String REST_URL = ProfileVoteRestController.REST_URL + '/';

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
    void getByRestaurantAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurant")
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allForAuth()));
    }

    @Test
    void getByDateForAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "date")
                .param("localDate", "2020-07-30")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE7));
    }

    @Test
    void getAllForAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allForAdmin()));
    }


    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "between")
                .param("startDate", "2020-06-28")
                .param("endDate", "2020-06-29")
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
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
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
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
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
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
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
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .param("voteId", String.valueOf(NOT_FOUND))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void create() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        log.info("created {}", created);
        int newId = created.id();
        newVote.setId(newId);
        newVote.setLocalDate(created.getLocalDate());
        VOTE_MATCHER.assertMatch(created, newVote);
    }

    @Test
    void delete() throws Exception {
        setСhangeVoteTime(TIME_TEST_IN);
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
