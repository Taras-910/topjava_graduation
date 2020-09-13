package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuUtil {

    public static List<Menu> toListMenus(List<Restaurant> restaurants, boolean toVote) {
        return restaurants.stream()
                .map(restaurant -> toMenu(restaurant, toVote))
                .collect(Collectors.toList());
    }
    public static Menu toMenu (Restaurant restaurant, boolean toVote){
        return new Menu(restaurant.getId(), restaurant.getName(), restaurant.getDishes(), false);
    }

    public static boolean countWithin(List<Dish> newDishes, List<Dish> storedDishes){
        return storedDishes == null ? newDishes.size() >=2 && newDishes.size() <=5 :
                newDishes.size() + storedDishes.size() >=2 && newDishes.size() + storedDishes.size() <=5;
    }

    public static boolean countWithin(int newDishes, List<Dish> storedDishes){
        return storedDishes == null ? newDishes >=2 && newDishes <=5 :
                newDishes + storedDishes.size() >=2 && newDishes + storedDishes.size() <=5;
    }

    public static boolean countLowerLimit(List<Dish> newDishes){
        return newDishes.size() > 2;
    }
}
