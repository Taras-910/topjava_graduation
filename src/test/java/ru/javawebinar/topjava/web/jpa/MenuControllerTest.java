package ru.javawebinar.topjava.web.jpa;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import java.util.List;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.testdata.DishTestData.*;
import static ru.javawebinar.topjava.testdata.MenuTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.*;
import static ru.javawebinar.topjava.testdata.UserTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.util.MenuUtil.toListMenus;
import static ru.javawebinar.topjava.util.MenuUtil.toMenu;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthorizedUserTest;

public class MenuControllerTest extends AbstractJpaControllerTest {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantController restaurantController;
    @Autowired
    private DishController dishController;
    @Autowired
    MenuController menuController;

    @Test
    public void getMenusToday() {
        setThisDay(DATE_TEST);
        List<Menu> menus = toListMenus(restaurantController.getAllWithDishesOfDate(thisDay), false, thisDay);
        MENU_MATCHER.assertMatch(menus, allMenusOfDay());
    }

    @Test
    public void getByRestaurantId() {
        setThisDay(DATE_TEST);
        Menu menu = toMenu(restaurantController.getByIdWithDishesOfDate(RESTAURANT1_ID, thisDay), false, thisDay);
        MENU_MATCHER.assertMatch(menu, MENU);
    }

    @Test
    public void getByRestaurantIdAndDate() {
        setThisDay(DATE_TEST);
        setAuthorizedUserTest(ADMIN);
        MENU_MATCHER.assertMatch(MENU, menuController.getByRestaurantIdAndDate(RESTAURANT1_ID, DATE_TEST));
    }

    @Test
    public void getByRestaurantNameAndDate() {
        setThisDay(DATE_TEST);
        setAuthorizedUserTest(ADMIN);
        MENU_MATCHER.assertMatch(MENU, menuController.getByRestaurantNameAndDate(RESTAURANT1.getName(), DATE_TEST));
    }

    @Test
    public void getErrorData() throws Exception {
        setAuthorizedUserTest(USER);
        setСhangeVoteTime(TIME_TEST_IN);
        assertThrows(NotFoundException.class,
                () -> toMenu(restaurantController.getByIdWithDishesOfDate(NOT_FOUND, thisDay), false, thisDay));
        assertThrows(NotFoundException.class,
                () -> menuController.getByRestaurantIdAndDate(NOT_FOUND, DATE_TEST));
        assertThrows(NotFoundException.class,
                () -> menuController.getByRestaurantIdAndDate(RESTAURANT1_ID, null));
         assertThrows(NotFoundException.class,
                () -> menuController.getByRestaurantNameAndDate(null, DATE_TEST));
    }

    @Test
    public void deleteRestaurantAndDishes() {
        menuController.deleteRestaurantAndDishes(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> menuController.getMenuByRestaurantId(RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> dishController.get(DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> menuController.deleteRestaurantAndDishes(NOT_FOUND));
    }

    @Test
    public void deleteListOfMenu() {
        menuController.deleteListOfMenu(RESTAURANT1_ID, DATE_TEST);
        assertThrows(NotFoundException.class, () -> menuController.getByRestaurantIdAndDate(RESTAURANT1_ID, DATE_TEST));
        assertThrows(NotFoundException.class, () -> dishController.getByRestaurantAndDate(RESTAURANT1_ID, DATE_TEST));
    }

    @Test
    public void deleteDishOfMenu() {
        setThisDay(DATE_TEST);
        menuController.deleteDishOfMenu(DISH10_ID, RESTAURANT2_ID);
        assertThrows(NotFoundException.class, () -> dishController.get(DISH10_ID, RESTAURANT2_ID));
    }

    @Test
    public void deleteErrorData() throws Exception {
        setAuthorizedUserTest(USER);
        setСhangeVoteTime(TIME_TEST_IN);
        assertThrows(NotFoundException.class, () -> menuController.deleteListOfMenu(NOT_FOUND, DATE_TEST));
        assertThrows(NotFoundException.class, () -> menuController.deleteListOfMenu(RESTAURANT1_ID, now()));
        assertThrows(NotFoundException.class, () -> menuController.deleteDishOfMenu(NOT_FOUND, RESTAURANT2_ID));
        assertThrows(NotFoundException.class, () -> menuController.deleteDishOfMenu(DISH10_ID, NOT_FOUND));
        assertThrows(NotFoundException.class, () -> menuController.deleteDishOfMenu(DISH10_ID, NOT_FOUND));
    }

    @Test
    public void createErrorDate() {
        assertThrows(NotFoundException.class, () -> menuController.createNewRestaurantByNameWithDishes(getNewDishes(), null));
        assertThrows(NotFoundException.class, () -> menuController.createNewRestaurantByNameWithDishes(null, "Пекин"));
        assertThrows(NotFoundException.class, () -> menuController.createNewRestaurantWithDishes(null));
        assertThrows(NotFoundException.class, () -> menuController.addListOfMenuForRestaurantId(null, RESTAURANT2_ID));
        assertThrows(NotFoundException.class, () -> menuController.addListOfMenuForRestaurantId(newDishes(), NOT_FOUND));
        assertThrows(NotFoundException.class, () -> menuController.addListOfMenuForRestaurantName(null, RESTAURANT2.getName()));
        assertThrows(NotFoundException.class, () -> menuController.addListOfMenuForRestaurantName(newDishes(), null));

    }
}
