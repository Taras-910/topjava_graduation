package ru.javawebinar.topjava.web.dish;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.testdata.DishTestData;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.web.testdata.DishTestData.*;
import static ru.javawebinar.topjava.web.testdata.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.topjava.web.testdata.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.web.testdata.UserTestData.USER_ID;

public class DishControllerTest extends AbstractControllerTest {

    @Autowired
    private DishController controller;

    @Test
    public void get() throws Exception {
        Dish actual = controller.get(DISH1_ID, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(actual, DISH1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.get(DishTestData.NOT_FOUND, RESTAURANT1_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.get(DISH1_ID, RESTAURANT2_ID));
    }

    @Test
    public void delete() throws Exception {
        controller.delete(DISH1_ID, RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> controller.get(DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    public void getAll() throws Exception {
        DishTestData.DISH_MATCHER.assertMatch(controller.getAll(RESTAURANT2_ID), DISHES_RESTAURANT2);
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(DishTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(DISH1_ID, ADMIN_ID));
    }

    @Test
    public void create() throws Exception {
        Dish created = controller.create(DishTestData.getNew(), RESTAURANT1_ID);
        int newId = created.id();
        Dish newDish = DishTestData.getNew();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(controller.get(newId, RESTAURANT1_ID), newDish);
    }

    @Test
    public void duplicateDishCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                controller.create(new Dish(null,"Суп", of(2020, Month.JUNE, 29), 0.1F), RESTAURANT1_ID));
    }

    @Test
    public void createAll() {
        List<Dish> created = controller.createAll(getNewList(), RESTAURANT2_ID);
        List<Dish> actual = getNewList();
        for(int i = 0; i < created.size(); i++) {
            actual.get(i).setId(created.get(i).getId());
        }
        DISH_MATCHER.assertMatch(created, actual);
    }

    @Test
    public void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        controller.update(updated, DISH1_ID, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(controller.get(DISH1_ID, RESTAURANT1_ID), DishTestData.getUpdated());
    }

    @Test
    public void updateNotOwn() throws Exception {
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                controller.update(DISH1, DISH1_ID ,RESTAURANT2_ID));
    }
}

