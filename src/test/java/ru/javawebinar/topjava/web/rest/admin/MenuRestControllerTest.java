package ru.javawebinar.topjava.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.json.JsonUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.testdata.DishTestData.DISH_MATCHER;
import static ru.javawebinar.topjava.testdata.MenuTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.USER;

class MenuRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MenuRestController.REST_URL + '/';

    @Autowired
    private MenuRestController controller;

    @Test
    void getAllToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(allMenusOfDay()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByRestaurantNameWithDishesAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurants/names/" + RESTAURANT1.getName())
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(menuOfDay()))
                .param("date", "2020-07-30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByRestaurantIdAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(menuOfDay()))
                .param("date", "2020-07-30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "date/2020-07-30")
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(allMenusOfDay()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteByRestaurantAndDate() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "restaurants/" + RESTAURANT1_ID + "/date/2020-06-29")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.getByRestaurantIdAndDate(RESTAURANT1_ID, of(2020,6,29)));
    }

    @Test
    void createMenuForNewRestaurant() throws Exception {
        List <Dish> created = getNewList();
        MvcResult action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "НовыйРесторан")
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(created)))
                .andExpect(status().isCreated())
                .andReturn();
        List<Dish> fromJson = readListFromJsonMvcResult(action, Dish.class);
        for(int i = 0; i < fromJson.size(); i++) {
            created.get(i).setId(fromJson.get(i).getId());
        }
        DISH_MATCHER.assertMatch(created, fromJson);
    }

    @Test
    void updateCreateMenuForRestaurantId() throws Exception {
        List <Dish> updated = getUpdatedList();
        MvcResult action = perform(MockMvcRequestBuilders.put(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isCreated())
                .andReturn();
        List<Dish> fromJson = readListFromJsonMvcResult(action, Dish.class);
        for(int i = 0; i < fromJson.size(); i++) {
            updated.get(i).setId(fromJson.get(i).getId());
        }
        DISH_MATCHER.assertMatch(updated, fromJson);
    }
}
