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
import static ru.javawebinar.topjava.util.DateTimeUtil.*;

public class RestaurantControllerTest extends AbstractJpaControllerTest {

    @Autowired
    RestaurantController controller;

    @Test
    public void findById() throws Exception {
        Assert.assertEquals(controller.getById(RESTAURANT1_ID), restaurant1WithDishes());
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND));
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
    public void getAll() throws Exception {
        List<Restaurant> all = controller.getAll();
        RESTAURANT_MATCHER.assertMatch(all, RESTAURANT1, RESTAURANT2);
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
}
