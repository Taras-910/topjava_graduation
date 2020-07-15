package ru.javawebinar.topjava.web.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.web.testdata.MenuTestData.newRestaurantWithDishes;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsComparator("dishes");

    public static final int NOT_FOUND = 100;
    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = RESTAURANT1_ID + 1;
    public static final int DISH1_ID = START_SEQ + 4;
    public static final Restaurant NEW_RESTAURANT = newRestaurantWithDishes();

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Дубок");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Прага");

    public static final Dish DISH1 = new Dish(DISH1_ID,"Суп", of(2020, Month.JUNE, 29), 0.1F);
    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "Чай",of(2020, Month.JUNE, 29), 0.2F);
    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "Борщ",of(2020, Month.JULY, 30), 1.1F);
    public static final Dish DISH4 = new Dish(DISH1_ID + 3, "Компот",of(2020, Month.JULY, 30), 1.2F);
    public static final Dish DISH5 = new Dish(DISH1_ID + 4, "еда",of(2020, Month.JULY, 30), 1.1F);
    public static final Dish DISH6 = new Dish(DISH1_ID + 5, "вода",of(2020, Month.JULY, 30), 0.2F);
    public static final Dish DISH7 = new Dish(DISH1_ID + 6, "граничная еда",of(2020, Month.JULY, 30), 2.1F);
    public static final Dish DISH8 = new Dish(DISH1_ID + 7, "Отбивная",of(2020, Month.JULY, 30), 2.2F);
    public static final Dish DISH9 = new Dish(DISH1_ID + 8, "Фрукты",of(2020, Month.JULY, 30), 2.3F);
    public static final Dish DISH10 = new Dish(DISH1_ID + 9, "Сладости",of(2020, Month.JULY, 30), 2.4F);
    public static final Dish DISH11 = new Dish(DISH1_ID + 10, "сок",of(2020, Month.JULY, 30), 2.5F);

    public static List<Restaurant> restaurantsWithDishes() {
        RESTAURANT1.setDishes(Arrays.asList(DISH5, DISH6));
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
