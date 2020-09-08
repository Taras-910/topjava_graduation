package ru.javawebinar.topjava.web.jpa.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;
import ru.javawebinar.topjava.web.jpa.MenuController;

import java.util.List;

import static ru.javawebinar.topjava.testdata.MenuTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;

public class AnonymousMenuControllerTest extends AbstractJpaControllerTest {

    @Autowired
    MenuController controller;

    @Test
    public void getAllMenus() {
        List<Menu> menus = controller.getMenusToday();
        MENU_MATCHER.assertMatch(allMenusOfDay(), menus);
    }

    @Test
    public void getMenuByRestaurantId() {
        Menu menu = controller.getMenuByRestaurantId(RESTAURANT1_ID);
        MENU_MATCHER.assertMatch(menuOfDay(), menu);
    }
}
