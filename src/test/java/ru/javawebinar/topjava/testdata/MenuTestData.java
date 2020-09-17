package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.to.Menu;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.javawebinar.topjava.testdata.DishTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2;

public class MenuTestData {
    public static TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingFieldsComparator(Menu.class, "dishes");
    public static final Menu MENU = testMenu();
    public static Dish getNew() {
        return new Dish("Созданный ужин", of(2020, Month.FEBRUARY, 1), 5.0F);
    }

    public static Menu testMenu() {
        RESTAURANT1.setDishes(Arrays.asList(DISH5, DISH6));
        return new Menu(RESTAURANT1.id(), RESTAURANT1.getName(), RESTAURANT1.getDishes(), false);
    }

    public static List<Menu> allMenusOfDay() {
        RESTAURANT1.setDishes(Arrays.asList(DISH5, DISH6));
        RESTAURANT2.setDishes(Arrays.asList(DISH7, DISH8, DISH9, DISH10, DISH11));
        Menu MENU1 = new Menu(RESTAURANT1.id(), RESTAURANT1.getName(), RESTAURANT1.getDishes(), false);
        Menu MENU2 = new Menu(RESTAURANT2.id(), RESTAURANT2.getName(), RESTAURANT2.getDishes(), false);
        return Arrays.asList(MENU1, MENU2);
    }
}
