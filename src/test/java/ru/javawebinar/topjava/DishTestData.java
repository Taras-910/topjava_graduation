package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Dish;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator("user");

    public static final int NOT_FOUND = 100;
    public static final int DISH1_ID = START_SEQ + 4;
    public static final int RESTAURANT_ID = START_SEQ + 2;

    public static final Dish DISH1 = new Dish(DISH1_ID, "Суп", of(2020, Month.JUNE, 29), 0.1F);
    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "Чай",of(2020, Month.JUNE, 29), 0.2F);
    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "Борщ",of(2020, Month.JUNE, 30), 1.1F);
    public static final Dish DISH4 = new Dish(DISH1_ID + 3, "Компот",of(2020, Month.JUNE, 30), 1.2F);

    public static final Dish DISH5 = new Dish(DISH1_ID + 4, "граничная еда",of(2020, Month.JUNE, 30), 2.1F);
    public static final Dish DISH6 = new Dish(DISH1_ID + 5, "Отбивная",of(2020, Month.JUNE, 30), 2.2F);
    public static final Dish DISH7 = new Dish(DISH1_ID + 6, "Фрукты",of(2020, Month.JUNE, 30), 2.3F);
    public static final Dish DISH8 = new Dish(DISH1_ID + 7, "Сладости",of(2020, Month.JUNE, 30), 2.4F);
    public static final Dish DISH9 = new Dish(DISH1_ID + 8, "сок",of(2020, Month.JUNE, 30), 2.5F);

//    public static final Menu MENU0 = new Menu(id, RESTAURANT1, List.of(DISH1, DISH2), false);
//    public static final Menu MENU1 = new Menu(id, RESTAURANT1, List.of(DISH3, DISH4), false);
//    public static final Menu MENU2 = new Menu(id, RESTAURANT1, List.of(DISH5, DISH6, DISH7, DISH8, DISH9), false);

    public static final List<Dish> DISHES1 = List.of(DISH2, DISH1);
    public static final List<Dish> DISHES2 = List.of(DISH4, DISH3);
    public static final List<Dish> DISHES3 = List.of(DISH9, DISH8, DISH7, DISH6, DISH5);


    public static Dish getNew() {
        return new Dish("Созданный ужин", of(2020, Month.FEBRUARY, 1), 5.0F);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленный завтрак", of(2020, Month.FEBRUARY, 1), 7.0F);
    }
}
