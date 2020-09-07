package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Dish;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator(Dish.class,"restaurant");

    public static final int NOT_FOUND = 100;
    public static final int DISH1_ID = START_SEQ + 4;
    public static final int DISH10_ID = DISH1_ID + 9;

    public static final Dish DISH1 = new Dish(DISH1_ID,"Суп", of(2020, Month.JUNE, 29), 0.1F);
    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "Чай",of(2020, Month.JUNE, 29), 0.2F);
    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "Борщ",of(2020, Month.JULY, 29), 1.1F);
    public static final Dish DISH4 = new Dish(DISH1_ID + 3, "Компот",of(2020, Month.JULY, 29), 1.2F);
    public static final Dish DISH5 = new Dish(DISH1_ID + 4, "еда",of(2020, Month.JULY, 30), 1.1F);
    public static final Dish DISH6 = new Dish(DISH1_ID + 5, "вода",of(2020, Month.JULY, 30), 0.2F);
    public static final Dish DISH7 = new Dish(DISH1_ID + 6, "граничная еда",of(2020, Month.JULY, 30), 2.1F);
    public static final Dish DISH8 = new Dish(DISH1_ID + 7, "Отбивная",of(2020, Month.JULY, 30), 2.2F);
    public static final Dish DISH9 = new Dish(DISH1_ID + 8, "Фрукты",of(2020, Month.JULY, 30), 2.3F);
    public static final Dish DISH10 = new Dish(DISH1_ID + 9, "Сладости",of(2020, Month.JULY, 30), 2.4F);
    public static final Dish DISH11 = new Dish(DISH1_ID + 10, "сок",of(2020, Month.JULY, 30), 2.5F);
    public static final Dish DISH12 = new Dish("Созданный пирог", of(2020, Month.FEBRUARY, 1), 5.0F);
    public static final Dish DISH13 = new Dish("Созданный чай", of(2020, Month.FEBRUARY, 1), 5.0F);
    public static final Dish DISH14 = new Dish(null, "Created еда3",of(2020, Month.JULY, 30), 2.2F);
    public static final Dish DISH15 = new Dish(null, "Created еда4",of(2020, Month.JULY, 30), 2.3F);
    public static final Dish DISH16 = new Dish(null, "Created еда5",of(2020, Month.JULY, 30), 2.4F);
    public static final Dish DISH17 = new Dish(null, "Created еда6",of(2020, Month.JULY, 30), 2.5F);

    public static final List<Dish> DISHES_RESTAURANT2 = List.of(DISH7, DISH8, DISH9, DISH10, DISH11);
    public static final List<Dish> DISHES_GET_ALL = List.of(DISH1, DISH2, DISH3, DISH4, DISH5, DISH6, DISH7, DISH8, DISH9, DISH10, DISH11);
    public static final List<Dish> DISHES_RESTAURANT_DATE = List.of(DISH6, DISH5);

    public static Dish getNew() {
        return new Dish("Созданный ужин", of(2020, Month.FEBRUARY, 1), 5.0F);
    }
    public static List<Dish> getNewList() {
        return Arrays.asList(DISH12, DISH13);
    }

    public static List<Dish> getNewDishes() {
        return Arrays.asList(DISH14, DISH15, DISH16);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленный завтрак", of(2020, Month.FEBRUARY, 1), 7.0F);
    }
}
