package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.CounterUtil.id;

public class MenuUtil {

    public static List<Menu> toListMenus(List<Restaurant> restaurants, List<Vote> votes, LocalDate date) {
        return restaurants.stream()
                .map(restaurant -> toMenu(restaurant, votes, date))
                .collect(Collectors.toList());
    }
    public static Menu toMenu (Restaurant restaurant, List<Vote> votes, LocalDate date){
        return new Menu(id(), restaurant, date, votes, false);
    }

    public static void checkNew(Menu menu) {
        if (menu.getId() != null) {
            throw new IllegalArgumentException("menu must be new (id=null)");
        }
    }

    public static boolean countWithin(List<Dish> newDishes, List<Dish> storedDishes){
       return storedDishes == null ? newDishes.size() >=2 && newDishes.size() <=5 :
               newDishes.size() + storedDishes.size() >=2 && newDishes.size() + storedDishes.size() <=5;
    }

    public static boolean countLowerLimit(List<Dish> newDishes){
        return newDishes.size() == 2;
    }
}
