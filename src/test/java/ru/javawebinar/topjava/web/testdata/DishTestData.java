package ru.javawebinar.topjava.web.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Dish;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator("restaurant");

    public static final int NOT_FOUND = 100;
    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int DISH1_ID = START_SEQ + 4;

    public static final Dish DISH1 = new Dish(DISH1_ID,"Суп", of(2020, Month.JUNE, 29), 0.1F);
    public static final Dish DISH7 = new Dish(DISH1_ID + 6, "граничная еда",of(2020, Month.JULY, 30), 2.1F);
    public static final Dish DISH8 = new Dish(DISH1_ID + 7, "Отбивная",of(2020, Month.JULY, 30), 2.2F);
    public static final Dish DISH9 = new Dish(DISH1_ID + 8, "Фрукты",of(2020, Month.JULY, 30), 2.3F);
    public static final Dish DISH10 = new Dish(DISH1_ID + 9, "Сладости",of(2020, Month.JULY, 30), 2.4F);
    public static final Dish DISH11 = new Dish(DISH1_ID + 10, "сок",of(2020, Month.JULY, 30), 2.5F);

    public static final List<Dish> DISHES_RESTAURANT2 = List.of(DISH7, DISH8, DISH9, DISH10, DISH11);

    public static Dish getNew() {
        return new Dish("Созданный ужин", of(2020, Month.FEBRUARY, 1), 5.0F);
    }
    public static List<Dish> getNewList() {
        return Arrays.asList(new Dish("Созданный пирог", of(2020, Month.FEBRUARY, 1), 5.0F),
                new Dish("Созданный чай", of(2020, Month.FEBRUARY, 1), 5.0F));
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленный завтрак", of(2020, Month.FEBRUARY, 1), 7.0F);
    }
}
