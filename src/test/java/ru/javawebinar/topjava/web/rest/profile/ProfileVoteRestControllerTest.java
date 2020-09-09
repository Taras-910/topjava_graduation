package ru.javawebinar.topjava.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.json.JsonUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.testdata.UserTestData.USER;
import static ru.javawebinar.topjava.testdata.VoteTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.TIME_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setСhangeVoteTime;

class ProfileVoteRestControllerTest extends AbstractControllerTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String REST_URL = ProfileVoteRestController.REST_URL + '/';

    @Autowired
    private ProfileVoteRestController controller;

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
    void getByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allForAuth()));
    }

    @Test
    void getByDateForAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "date/2020-07-30")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE7));
    }


    @Test
    void isExistVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "exist/date/2020-07-30")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertTrue(controller.isExistVote(LocalDate.of(2020,07,30)));
    }
    @Test
    void getAllForAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "auth")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(allForAdmin()));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "between")
                .with(userHttpBasic(USER))
                .param("startDate", "2020-06-28")
                .param("endDate", "2020-06-29")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(between()));
    }

    @Test
    void update() throws Exception {
        setСhangeVoteTime(TIME_TEST);
        DateTimeUtil.setСhangeVoteTime(LocalTime.of(23, 59));
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(controller.get(VOTE1_ID), updated);
    }

    @Test
    void create() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        log.info("created {}", created);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(controller.get(newId), newVote);
    }

    @Test
    void delete() throws Exception {
        setСhangeVoteTime(TIME_TEST);
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.get(VOTE1_ID));
    }
}
