package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Dish;

import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.DateTimeUtil.toDate;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator(Dish.class,"restaurant");

    public static final int DISH1_ID = START_SEQ + 4;
    public static final int DISH10_ID = DISH1_ID + 9;

    public static final Dish DISH1 = new Dish(DISH1_ID,"Суп", toDate(2020, 06, 29), 10);
    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "Чай", toDate(2020, 06, 29), 20);
    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "Борщ", toDate(2020, 07, 29), 110);
    public static final Dish DISH4 = new Dish(DISH1_ID + 3, "Компот", toDate(2020, 07, 29), 120);
    public static final Dish DISH5 = new Dish(DISH1_ID + 4, "еда", toDate(2020, 07, 30), 110);
    public static final Dish DISH6 = new Dish(DISH1_ID + 5, "вода", toDate(2020, 07, 30), 20);
    public static final Dish DISH7 = new Dish(DISH1_ID + 6, "вкусная еда", toDate(2020, 07, 30), 210);
    public static final Dish DISH8 = new Dish(DISH1_ID + 7, "Отбивная", toDate(2020, 07, 30), 220);
    public static final Dish DISH9 = new Dish(DISH1_ID + 8, "Фрукты", toDate(2020, 07, 30), 230);
    public static final Dish DISH10 = new Dish(DISH1_ID + 9, "Сладости", toDate(2020, 07, 30), 240);
    public static final Dish DISH11 = new Dish(DISH1_ID + 10, "сок", toDate(2020, 07, 30), 250);
    public static final List<Dish> DISHES_GET_ALL = List.of(DISH1, DISH2, DISH3, DISH4, DISH5, DISH6, DISH7, DISH8, DISH9, DISH10, DISH11);

    public static final List<Dish> DISHES_RESTAURANT_DATE = List.of(DISH6, DISH5);

    public static Dish getNew() {
        return new Dish("Созданный ужин",  toDate(2020, 07, 30), 500);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленный завтрак",  new Date(2020, 02, 1), 700);
    }
}
