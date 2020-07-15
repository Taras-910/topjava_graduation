package ru.javawebinar.topjava.web.admin;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.dish.DishController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.web.testdata.DishTestData.DISH1_ID;
import static ru.javawebinar.topjava.web.testdata.MenuTestData.*;
import static ru.javawebinar.topjava.web.testdata.RestaurantTestData.NEW_RESTAURANT;

public class MenuControllerTest extends AbstractControllerTest {

    @Autowired
    MenuController controller;

    @Autowired
    DishController dishController;

    @Test
    public void getAllMenusOfDay() {
        List<Menu> menus = controller.getAllMenusOfDay();
        MENU_MATCHER.assertMatch(menus, allMenusOfDay());
    }

    @Test
    public void getMenuByRestaurantId() {
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
