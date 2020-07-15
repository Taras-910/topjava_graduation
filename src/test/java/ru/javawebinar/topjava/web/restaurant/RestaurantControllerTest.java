package ru.javawebinar.topjava.web.restaurant;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.web.testdata.RestaurantTestData.*;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantController controller;

    @Test
    public void findById() throws Exception {
        Assert.assertEquals(controller.findById(RESTAURANT1_ID), restaurant1WithDishes());
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.findById(NOT_FOUND));
    }

    @Test
    public void delete() throws Exception {
        controller.delete(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> controller.findById(RESTAURANT1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void update() throws Exception {
        Restaurant updated = getUpdated();
        controller.update(updated, RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(controller.findById(RESTAURANT1_ID), getUpdated());
    }

    @Test
    public void getAll() throws Exception {
        List<Restaurant> all = controller.getAll();
        RESTAURANT_MATCHER.assertMatch(all, RESTAURANT1, RESTAURANT2);
    }

    @Test
    public void getAllByDateWithDishes() throws Exception {
        List<Restaurant> restaurant = controller.getAllByDateWithDishes(thisDay);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurantsWithDishes());
    }

    @Test
    public void getByIdAndDate() throws Exception {
        Restaurant restaurant = controller.getByIdAndDate(RESTAURANT1_ID, thisDay);
        RESTAURANT_MATCHER.assertMatch(restaurant, RESTAURANT1);
    }
}
