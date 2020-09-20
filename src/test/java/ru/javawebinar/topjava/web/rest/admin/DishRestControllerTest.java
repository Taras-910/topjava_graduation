package ru.javawebinar.topjava.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.testdata.DishTestData;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.json.JsonUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.*;
import static ru.javawebinar.topjava.testdata.DishTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.testdata.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setThisDay;

class DishRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = DishRestController.REST_URL + '/';
    private static Logger log = LoggerFactory.getLogger(DishRestControllerTest.class);
    @Autowired
    private DishRestController controller;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID + "/restaurants/" + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH1));
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND, RESTAURANT1_ID));
    }

    @Test
    void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.getById(DISH1_ID, RESTAURANT2_ID));
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
                .param("date", valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(DISH_MATCHER.contentJson(DISHES_RESTAURANT_DATE));
    }

    @Test
    void createLimit() throws Exception {
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
        Dish newDish = new Dish(null, DATE_TEST, 1.1F);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createErrorDishPrice() throws Exception {
        Dish newDish = new Dish("tea", DATE_TEST, -1.1F);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPriceExceedValue() throws Exception {
        Dish newDish = new Dish("tea", DATE_TEST, 100.1F);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createErrorId() throws Exception {
        DateTimeUtil.setThisDay(LocalDate.now().minusDays(1));
        Dish newDish = new Dish(DISH1_ID, "tea", DATE_TEST, 1.1F);
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
        DateTimeUtil.setThisDay(LocalDate.now().minusDays(1));
        Dish newDish = new Dish(null, "tea", null, 1.1F);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createListOfMenu() throws Exception {
        List<Dish> newDishes = getNewList();
        MvcResult action = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/100002")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDishes)))
                .andDo(print())
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
    void createUpLimit() throws Exception {
        List<Dish> newDishes = overLimitMax();
        perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDishes)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createDownLimit() throws Exception {
        List<Dish> newDishes = overLimitMin();
        perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDishes)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createDuplicate() throws Exception {
        List<Dish> newDishes = asList(DISH1, DISH1);
        perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDishes)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void createNotFound() throws Exception {
        List<Dish> newDishes = asList(DISH1, DISH2);
        perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDishes)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void createErrorDish() throws Exception {
        List<Dish> newDishes = asList(null, DISH2);
        perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDishes)))
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
                .andExpect(status().isCreated());
        DISH_MATCHER.assertMatch(controller.getById(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    void updateNotOwn() throws Exception {
        Dish updated = new Dish(DISH1_ID, "Обновленный завтрак", DATE_TEST, 7.0F);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
                .param("restaurantId", valueOf(RESTAURANT2_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isBadRequest());
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
                .param("date", String.valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.getById(DISH10_ID, RESTAURANT2_ID));
    }

    @Test
    void deleteOverLimit() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID + "/restaurants/" + RESTAURANT1_ID)
                .param("date", String.valueOf(DATE_TEST))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
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

    @Test
    void deleteListOfMenu() throws Exception  {
        perform(MockMvcRequestBuilders.delete(REST_URL + "restaurants/" + RESTAURANT1_ID)
                .param("date", "2020-06-29").with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.getByRestaurantAndDate(RESTAURANT1_ID, LocalDate.of(2020,06,29)));
    }
}
