package ru.javawebinar.topjava.web.testdata;

import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.Menu;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.web.testdata.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.web.testdata.UserTestData.USER_ID;

public class MenuTestData {
    public static TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingFieldsComparator("id");
    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = RESTAURANT1_ID + 1;
    public static final int DISH1_ID = START_SEQ + 4;
    public static final int VOTE1_ID = START_SEQ + 15;

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

    public static final Dish DISH12 = new Dish(null, "еда1",of(2020, Month.JULY, 30), 0.2F);
    public static final Dish DISH13 = new Dish(null, "еда2",of(2020, Month.JULY, 30), 2.1F);
    public static final Dish DISH14 = new Dish(null, "еда3",of(2020, Month.JULY, 30), 2.2F);
    public static final Dish DISH15 = new Dish(null, "еда4",of(2020, Month.JULY, 30), 2.3F);
    public static final Dish DISH16 = new Dish(null, "еда5",of(2020, Month.JULY, 30), 2.4F);
    public static final Dish DISH17 = new Dish(null, "еда6",of(2020, Month.JULY, 30), 2.5F);

    public static final Vote VOTE1 = new Vote(VOTE1_ID, of(2020, Month.JUNE, 28), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote VOTE2 = new Vote(VOTE1_ID + 1, of(2020, Month.JUNE, 29), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote VOTE3 = new Vote(VOTE1_ID + 2, of(2020, Month.JUNE, 29), RESTAURANT1_ID, USER_ID);
    public static final Vote VOTE4 = new Vote(VOTE1_ID + 3, of(2020, Month.JUNE, 30), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote VOTE5 = new Vote(VOTE1_ID + 4, of(2020, Month.JUNE, 30), RESTAURANT2_ID, USER_ID);

    public static Dish getNew() {
        return new Dish("Созданный ужин", of(2020, Month.FEBRUARY, 1), 5.0F);
    }

    public static List<Menu> allMenusOfDay() {
        RESTAURANT1.setDishes(Arrays.asList(DISH5, DISH6));
        RESTAURANT2.setDishes(Arrays.asList(DISH7, DISH8, DISH9, DISH10, DISH11));
        Menu MENU1 = new Menu(1, RESTAURANT1, of(2020,7,30), Arrays.asList(VOTE1, VOTE2, VOTE4), false);
        Menu MENU2 = new Menu(2, RESTAURANT2, of(2020,7,30), Arrays.asList(VOTE1, VOTE2, VOTE4), false);
        return Arrays.asList(MENU1, MENU2);
    }

    public static Menu menuOfDay() {
        RESTAURANT1.setDishes(Arrays.asList(DISH5, DISH6));
        return new Menu(1, RESTAURANT1, of(2020,7,30), Arrays.asList(VOTE1, VOTE2, VOTE4), false);
    }
    public static Restaurant newRestaurantWithDishes(){
        Restaurant restaurant = new Restaurant(null,"Созданный ресторан");
        restaurant.setDishes(getNewList());
        return restaurant;
    }

    public static List<Dish> getNewList() {
        return Arrays.asList(new Dish("Созданный пирог", of(2020, Month.FEBRUARY, 1), 5.0F),
                new Dish("Созданный чай", of(2020, Month.FEBRUARY, 1), 5.0F));
    }

}
