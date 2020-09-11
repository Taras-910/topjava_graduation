package ru.javawebinar.topjava.web.jpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.testdata.DishTestData;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.testdata.DishTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;

public class DishControllerTest extends AbstractJpaControllerTest {

    @Autowired
    private DishController controller;

    @Test
    public void get() throws Exception {
        Dish actual = controller.get(DISH1_ID, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(actual, DISH1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND, RESTAURANT1_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.get(DISH1_ID, RESTAURANT2_ID));
    }

    @Test
    public void getAll() throws Exception {
        DISH_MATCHER.assertMatch(controller.getAll(), DISHES_ALL);
    }

    @Test
    public void getAllByRestaurant() throws Exception {
        DISH_MATCHER.assertMatch(controller.getAllByRestaurant(RESTAURANT2_ID), DISHES_RESTAURANT2);
    }

    @Test
    public void getByRestaurantAndDate() throws Exception {
        DISH_MATCHER.assertMatch(controller.getByRestaurantAndDate(RESTAURANT1_ID, DATE_TEST), DISHES_RESTAURANT_DATE);
    }

    @Test
    public void delete() throws Exception {
        DateTimeUtil.setThisDay(DATE_TEST);
        controller.delete(DISH10_ID, RESTAURANT2_ID);
        assertThrows(NotFoundException.class, () -> controller.get(DISH10_ID, RESTAURANT2_ID));
    }

    @Test
    public void deleteOverLimit() throws Exception {
        DateTimeUtil.setThisDay(DATE_TEST);
        assertThrows(NotFoundException.class, () -> controller.delete(DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        DateTimeUtil.setThisDay(DATE_TEST);
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND, RESTAURANT2_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        DateTimeUtil.setThisDay(DATE_TEST);
        assertThrows(NotFoundException.class, () -> controller.delete(DISH1_ID, RESTAURANT2_ID));
    }

    @Test
    public void deleteListOfMenu() throws Exception {
        controller.deleteListOfMenu(RESTAURANT1_ID, DATE_TEST);
        assertThrows(NotFoundException.class, () -> controller.getByRestaurantAndDate(RESTAURANT1_ID, DATE_TEST));
    }

    @Test
    public void create() throws Exception {
        DateTimeUtil.setThisDay(DATE_TEST);
        Dish created = controller.create(DishTestData.getNew(), RESTAURANT1_ID);
        int newId = created.id();
        Dish newDish = DishTestData.getNew();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(controller.get(newId, RESTAURANT1_ID), newDish);
    }

    @Test
    public void createDuplicate() throws Exception {
        DateTimeUtil.setThisDay(DATE_TEST);
        Dish created = new Dish(DISH1);
        created.setId(null);
        assertThrows(NotFoundException.class, () -> controller.create(created, RESTAURANT1_ID));
    }

    @Test
    public void createErrorData() throws Exception {
        DateTimeUtil.setThisDay(LocalDate.now().plusDays(1));
        assertThrows(NotFoundException.class, () -> controller.create(new Dish(DISH1_ID, "tea", DATE_TEST, 1.1F), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.create(new Dish(null, null, DATE_TEST, 1.1F), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.create(new Dish(null, "tea", null, 1.1F), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.create(new Dish(null, "tea", DATE_TEST, -1.0F), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.create(new Dish(null, "tea", DATE_TEST, 101.0F), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.create(new Dish(null, "tea", DATE_TEST, 1.0F), NOT_FOUND));
    }

    @Test
    public void createListOfMenu() throws Exception {
        List<Dish> actual = getNewDishes();
        List<Dish> created = controller.createListOfMenu(actual, RESTAURANT1_ID);
        for(int i = 0; i < created.size(); i++) {
            actual.get(i).setId(created.get(i).getId());
        }
        DISH_MATCHER.assertMatch(created, actual);
    }

    @Test
    public void createOverLimit() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.createListOfMenu(overLimitMax(), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.createListOfMenu(overLimitMin(), RESTAURANT2_ID));
    }

    @Test
    public void createListErrorData() throws Exception {
        DateTimeUtil.setThisDay(DATE_TEST);
        assertThrows(NotFoundException.class, () -> controller.createListOfMenu(asList(DISH1, DISH1), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.createListOfMenu(asList(null, DISH1), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.createListOfMenu(asList(DISH1, DISH2), NOT_FOUND));
        assertThrows(NotFoundException.class, () -> controller.createListOfMenu(null, RESTAURANT1_ID));
    }

    @Test
    public void update() throws Exception {
        DateTimeUtil.setThisDay(DATE_TEST);
        Dish updated = DishTestData.getUpdated();
        controller.update(updated, DISH1_ID, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(controller.get(DISH1_ID, RESTAURANT1_ID), DishTestData.getUpdated());
    }

    @Test
    public void updateNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.update(DISH1, DISH1_ID ,RESTAURANT2_ID));
    }

    @Test
    public void updateErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.update(DISH1, DISH10_ID ,RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.update(DISH10, DISH1_ID ,RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.update(null, DISH1_ID ,RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.update(DISH1, NOT_FOUND ,RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.update(DISH1, DISH1_ID ,NOT_FOUND));
    }
}

