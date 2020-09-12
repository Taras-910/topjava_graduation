package ru.javawebinar.topjava.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.testdata.DishTestData;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.json.JsonUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.*;
import static ru.javawebinar.topjava.testdata.DishTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setThisDay;

class DishRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = DishRestController.REST_URL + '/';

    @Autowired
    private DishRestController controller;

    @Test
    void get() throws Exception {
        MvcResult action = perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID + "/restaurants/" + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH1))
                .andReturn();
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISHES_GET_ALL));
    }

    @Test
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(controller.get(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    void create() throws Exception {
        setThisDay(DATE_TEST);
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());
        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(controller.get(newId, RESTAURANT1_ID), newDish);
    }

    @Test
    void createAll() throws Exception {
        List<Dish> newDishes = getNewList();
        MvcResult action = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/100002")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDishes)))
                .andExpect(status().isCreated())
                .andReturn();
        List<Dish> created = readListFromJsonMvcResult(action, Dish.class);
        for(int i = 0; i < created.size(); i++) {
            newDishes.get(i).setId(created.get(i).getId());
        }
        DISH_MATCHER.assertMatch(created, newDishes);
        List<Dish> formDB = controller.getByRestaurantAndDate(RESTAURANT1_ID, LocalDate.of(2020,02,01));
        DISH_MATCHER.assertMatch(formDB, newDishes);
    }
    @Test
    void getByRestaurantAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/restaurants/100002/date/2020-07-30")
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(DISH_MATCHER.contentJson(DISHES_RESTAURANT_DATE));
    }

    @Test
    void delete() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH10_ID)
                .param("restaurantId", valueOf(RESTAURANT2_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.get(DISH10_ID, RESTAURANT2_ID));
    }

    @Test
    void deleteListOfMenu() throws Exception  {
        perform(MockMvcRequestBuilders.delete(REST_URL + "restaurants/" + RESTAURANT1_ID + "/date/2020-06-29")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.getByRestaurantAndDate(RESTAURANT1_ID, LocalDate.of(2020,06,29)));
    }
}

