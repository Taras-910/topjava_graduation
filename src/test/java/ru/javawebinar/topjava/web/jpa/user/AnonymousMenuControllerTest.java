package ru.javawebinar.topjava.web.jpa.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import java.util.List;

import static ru.javawebinar.topjava.testdata.MenuTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setThisDay;

public class AnonymousMenuControllerTest extends AbstractJpaControllerTest {

    @Autowired
    AnonymousMenuController controller;

    @Test
    public void getAllMenus() {
        setThisDay(DATE_TEST);
        List<Menu> menus = controller.getMenusToday();
        MENU_MATCHER.assertMatch(allMenusOfDay(), menus);
    }

    @Test
    public void getMenuByRestaurantId() {
        setThisDay(DATE_TEST);
        Menu menuDB = controller.getMenuByRestaurantId(RESTAURANT1_ID);
        MENU_MATCHER.assertMatch(MENU, menuDB);
    }
}
