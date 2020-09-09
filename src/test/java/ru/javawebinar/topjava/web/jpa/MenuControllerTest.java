package ru.javawebinar.topjava.web.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.testdata.DishTestData.DISH1_ID;
import static ru.javawebinar.topjava.testdata.MenuTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.NEW_RESTAURANT;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setThisDay;

public class MenuControllerTest extends AbstractJpaControllerTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MenuController controller;

    @Autowired
    private DishController dishController;

    @Test
    public void getMenusToday() {
        List<Menu> menus = controller.getMenusToday();
        MENU_MATCHER.assertMatch(menus, allMenusOfDay());
    }

    @Test
    public void getMenuByRestaurantId() {
        setThisDay(DATE_TEST);
        Menu menu = controller.getMenuByRestaurantId(RESTAURANT1_ID);
        MENU_MATCHER.assertMatch(menu, menuOfDay());
    }

    @Test
    public void deleteRestaurantAndDishes() {
        controller.deleteRestaurantAndDishes(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> controller.getMenuByRestaurantId(RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> dishController.get(DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    public void createRestaurantAndDishes() {
        Restaurant testRestaurant = NEW_RESTAURANT;
        List<Dish> testDishes = testRestaurant.getDishes();
        Restaurant createdRestaurant = controller.createRestaurantAndDishes(testRestaurant);
        List<Dish> createdDishes = createdRestaurant.getDishes();
        for(int i = 0; i < createdDishes.size(); i++) {
            testDishes.get(i).setId(createdDishes.get(i).getId());
        }
        int newId = createdRestaurant.id();
        testRestaurant.setId(newId);
        testRestaurant.setDishes(testDishes);
        Assert.assertEquals(testRestaurant, createdRestaurant);
    }
}
