package ru.javawebinar.topjava.web.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.*;
import static ru.javawebinar.topjava.testdata.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.util.DateTimeUtil.*;

public class RestaurantControllerTest extends AbstractJpaControllerTest {

    @Autowired
    RestaurantController controller;

    @Test
    public void getById() throws Exception {
        Assert.assertEquals(controller.getById(RESTAURANT1_ID), RESTAURANT1);
    }

    @Test
    public void getByName() throws Exception {
        Assert.assertEquals(controller.getByName(RESTAURANT1.getName()), RESTAURANT1);
    }

    @Test
    public void getAll() throws Exception {
        List<Restaurant> all = controller.getAll();
        RESTAURANT_MATCHER.assertMatch(all, RESTAURANT1, RESTAURANT2);
    }

    @Test
    public void getAllWithDishes() throws Exception {
        Assert.assertEquals(controller.getById(RESTAURANT1_ID), restaurant1WithDishes());
    }

    @Test
    public void getAllWithDishesOfDate() throws Exception {
        List<Restaurant> restaurant = controller.getAllWithDishesOfDate(thisDay);
        RESTAURANT_MATCHER.assertMatch(restaurant, withDishesByDate());
    }

    @Test
    public void getByIdWithDishesOfDate() throws Exception {
        setThisDay(DATE_TEST);
        Restaurant restaurant = controller.getByIdWithDishesOfDate(RESTAURANT1_ID, thisDay);
        RESTAURANT_MATCHER.assertMatch(restaurant, RESTAURANT1);
    }

    @Test
    public void getErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND));
        assertThrows(NotFoundException.class, () -> controller.getByIdWithDishesOfDate(NOT_FOUND, thisDay));
        assertThrows(NotFoundException.class, () -> controller.getByIdWithDishesOfDate(RESTAURANT1_ID, null));
    }

    @Test
    public void delete() throws Exception {
        controller.delete(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> controller.getById(RESTAURANT1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void update() throws Exception {
        Restaurant updated = getUpdated();
        controller.update(updated, RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(controller.getById(RESTAURANT1_ID), getUpdated());
    }

    @Test
    public void updateErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.update(new Restaurant(null, "Новый"), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.update(new Restaurant(RESTAURANT1_ID, "Новый"), NOT_FOUND));
        assertThrows(NotFoundException.class, () -> controller.update(new Restaurant(null, null), RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> controller.update(new Restaurant(RESTAURANT1_ID, "Новый"), RESTAURANT2_ID));
    }

    @Test
    public void create() throws Exception {
        Restaurant newRestaurant = getNew();
        Restaurant created = controller.create(newRestaurant);
        newRestaurant.setId(created.getId());
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
    }

    @Test
    public void createErrorDate() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.create(new Restaurant(RESTAURANT1_ID, "Новый")));
        assertThrows(NotFoundException.class, () -> controller.create(new Restaurant(NOT_FOUND, "Новый")));
        assertThrows(NotFoundException.class, () -> controller.create(new Restaurant(RESTAURANT1_ID, null)));
    }
}
