package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator("user");

    public static final int NOT_FOUND = 100;
    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = RESTAURANT1_ID + 1;
    public static Restaurant NEW_RESTAURANT_WITH_DISHES = getNewRestaurantWithDishes();

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Дубок");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Прага");

    public static Restaurant getNewRestaurantWithDishes() {
        Restaurant newRestaurant = new Restaurant(null, "NewPizza");
        Dish dish1 = new Dish(null, "НовыйСуп", LocalDate.of(2020, Month.JUNE, 14), 0.1F);
        Dish dish2 = new Dish(null, "НовыйЧай", LocalDate.of(2020, Month.JUNE, 14), 0.2F);
        Dish dish3 = new Dish(null, "НовыйБорщ", LocalDate.of(2020, Month.JUNE, 14), 1.1F);
        List<Dish> dishes = Arrays.asList(dish1, dish2, dish3);
        newRestaurant.setDishes(dishes);
        return newRestaurant;
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "Созданный ресторан");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Обновленный ресторан");
    }
}
