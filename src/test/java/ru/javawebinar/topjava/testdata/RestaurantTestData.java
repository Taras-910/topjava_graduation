package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Restaurant;

import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.testdata.DishTestData.*;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsComparator(Restaurant.class,"dishes");
    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = START_SEQ + 3;
    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Дубок");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Прага");

    public static List<Restaurant> withDishesByDate() {
        RESTAURANT1.setDishes(Arrays.asList(DISH5, DISH6));
        RESTAURANT2.setDishes(Arrays.asList(DISH7, DISH8, DISH9, DISH10, DISH11));
        return Arrays.asList(RESTAURANT1, RESTAURANT2);
    }

    public static List<Restaurant> allWithDishes() {
        RESTAURANT1.setDishes(Arrays.asList(DISH1, DISH2, DISH3, DISH4, DISH5, DISH6));
        RESTAURANT2.setDishes(Arrays.asList(DISH7, DISH8, DISH9, DISH10, DISH11));
        return Arrays.asList(RESTAURANT1, RESTAURANT2);
    }

    public static Restaurant restaurant1WithDishes() {
        RESTAURANT1.setDishes(Arrays.asList(DISH1, DISH2, DISH3, DISH4, DISH5, DISH6));
        return RESTAURANT1;
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "Созданный ресторан");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Обновленный ресторан");
    }

}
