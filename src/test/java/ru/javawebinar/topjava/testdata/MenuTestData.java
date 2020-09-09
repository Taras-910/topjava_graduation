package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.javawebinar.topjava.testdata.DishTestData.*;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT2;

public class MenuTestData {
    public static TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingFieldsComparator(Menu.class,"voteAuth");

    public static Dish getNew() {
        return new Dish("Созданный ужин", of(2020, Month.FEBRUARY, 1), 5.0F);
    }

    public static Menu getNewMenu() {
        Restaurant newRestaurant = new Restaurant(3, "НовыйРесторан");
        Dish newDish = new Dish("Созданный ужин", of(2020, Month.FEBRUARY, 1), 5.0F);
        newRestaurant.setDishes(Arrays.asList(newDish));
        return new Menu(newRestaurant, of(2020,7,30), true, false);
    }

    public static List<Menu> allMenusOfDay() {
        RESTAURANT1.setDishes(Arrays.asList(DISH5, DISH6));
        RESTAURANT2.setDishes(Arrays.asList(DISH7, DISH8, DISH9, DISH10, DISH11));
        Menu MENU1 = new Menu(RESTAURANT1, of(2020,7,30), true, false);
        Menu MENU2 = new Menu(RESTAURANT2, of(2020,7,30), true, false);
        return Arrays.asList(MENU1, MENU2);
    }

    public static Menu menuOfDay() {
        RESTAURANT1.setDishes(Arrays.asList(DISH5, DISH6));
        return new Menu(RESTAURANT1, of(2020,7,30), true, false);
    }

    public static Menu byDate() {
        RESTAURANT1.setDishes(Arrays.asList(DISH3, DISH4));
        return new Menu(RESTAURANT1, of(2020,7,29), true, false);
    }


    public static Restaurant newRestaurantWithDishes(){
        Restaurant restaurant = new Restaurant(null,"Созданный ресторан");
        restaurant.setDishes(getNewList());
        return restaurant;
    }

    public static List<Dish> getNewList() {
        return Arrays.asList(
                new Dish("Созданный пирог", of(2020, Month.FEBRUARY, 1), 5.0F),
                new Dish("Созданный чай", of(2020, Month.FEBRUARY, 1), 5.0F));
    }

    public static List<Dish> getUpdatedList() {
        return Arrays.asList(
                new Dish("Обновленный завтрак", of(2020, 6, 29), 7.0F),
                new Dish("Обновленный обед", of(2020, 6, 29), 7.0F));
    }

}
