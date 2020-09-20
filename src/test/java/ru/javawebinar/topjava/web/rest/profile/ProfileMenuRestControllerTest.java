package ru.javawebinar.topjava.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.util.json.JsonUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.testdata.MenuTestData.MENU;
import static ru.javawebinar.topjava.testdata.MenuTestData.allMenusOfDay;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setThisDay;

class ProfileMenuRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileMenuRestController.REST_URL + '/';

    @Autowired
    private ProfileMenuRestController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(allMenusOfDay()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllToday() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.get(REST_URL + "today")
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(allMenusOfDay()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByRestaurantToday() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(MENU))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "date")
                .param("date", String.valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(allMenusOfDay()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByRestaurantNameAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "menu")
                .param("restaurantName", RESTAURANT1.getName())
                .param("date", String.valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(MENU))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByRestaurantIdAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "menu/" + RESTAURANT1_ID)
                .param("date", String.valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(MENU))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
