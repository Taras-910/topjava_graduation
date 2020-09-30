package ru.javawebinar.topjava.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.testdata.DishTestData;
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
import static ru.javawebinar.topjava.testdata.DishTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.testdata.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setThisDay;

class DishRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = DishRestController.REST_URL + '/';

    @Autowired
    private DishRestController controller;

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getNotOwn() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
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
    void getByRestaurantAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/menus")
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .param("localDate", "2020-07-30")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(DISH_MATCHER.contentJson(DISHES_RESTAURANT_DATE));
    }

    @Test
    void create() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated());
        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(controller.getById(newId, RESTAURANT1_ID), newDish);
    }

    @Test
    void createErrorDishName() throws Exception {
        Dish newDish = new Dish(null, DATE_TEST, 110);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createErrorDishPrice() throws Exception {
        Dish newDish = new Dish("tea", DATE_TEST, -110);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createPriceExceedValue() throws Exception {
        Dish newDish = new Dish("tea", DATE_TEST, 100001);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createErrorId() throws Exception {
        DateTimeUtil.setThisDay(new Date());
        Dish newDish = new Dish(DISH1_ID, "tea", DATE_TEST, 110);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void createErrorDate() throws Exception {
        DateTimeUtil.setThisDay(new Date());
        Dish newDish = new Dish(null, "tea", null, 110);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());
        DISH_MATCHER.assertMatch(controller.getById(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    void updateNotOwn() throws Exception {
        Dish updated = new Dish(DISH1_ID, "Обновленный завтрак", DATE_TEST, 700);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
                .param("restaurantId", valueOf(RESTAURANT2_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateNotFound() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void delete() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH10_ID + "/restaurants/" + RESTAURANT2_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.getById(DISH10_ID, RESTAURANT2_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND + "/restaurants/" + RESTAURANT1_ID)
                .param("date", String.valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteNotOwn() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID + "/restaurants/" + RESTAURANT2_ID)
                .param("date", String.valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
