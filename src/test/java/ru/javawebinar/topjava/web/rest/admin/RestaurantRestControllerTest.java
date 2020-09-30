package ru.javawebinar.topjava.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.json.JsonUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.*;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.testdata.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;

class RestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantRestController.REST_URL + '/';
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RestaurantRestController controller;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getByName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "names")
                .param("restaurantName", RESTAURANT2.getName())
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT2));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT1, RESTAURANT2));
    }

    @Test
    void getByIdWithDishesOfDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .param("localDate", String.valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1WithDishes()));
    }

    @Test
    void getAllWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "dishes")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(allWithDishes()));
    }

    @Test
    void getAllWithDishesOfDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "menus")
                .param("localDate", "2020-07-30")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(withDishesByDate()));
    }

    @Test
    void getErrorDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "menus")
                .param("date", "2020-20-20")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());
        RESTAURANT_MATCHER.assertMatch(controller.getById(RESTAURANT1_ID), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT1_ID, "Венеция Обновленная");
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateErrorName() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT1_ID, null);
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateNotOwn() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT1_ID, "Краков Обновленный");
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT2_ID)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict());
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());
        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(controller.getById(newId), newRestaurant);
    }

    @Test
    void createErrorName() throws Exception {
        Restaurant updated = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createDuplicate() throws Exception {
        Restaurant updated = new Restaurant(null, "Новый");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT2_ID)
                .param("restaurantId", valueOf(RESTAURANT2_ID))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.getById(RESTAURANT2_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }
}
