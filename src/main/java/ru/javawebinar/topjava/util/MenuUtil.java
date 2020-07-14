package ru.javawebinar.topjava.util;

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

    public static boolean countWithin(Restaurant restaurant, Restaurant repository){
       return repository == null ? count(restaurant) >=2 && count(restaurant) <=5 :
               commonCount(restaurant, repository) >= 2 && commonCount(restaurant, repository) <= 5;
    }

    public static int count(Restaurant restaurant){ return restaurant.getDishes().size(); }

    public static int commonCount(Restaurant restaurant, Restaurant repository){
        return restaurant.getDishes().size() + repository.getDishes().size(); }

    public static boolean countUpperLimit(Restaurant restaurant){
        return restaurant.getDishes().size() == 5;
    }

    public static boolean countLowerLimit(Restaurant restaurant){
        return restaurant.getDishes().size() == 2;
    }



}
