package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuUtil {

    public static List<Menu> toListMenus(List<Restaurant> restaurants, Vote vote) {
        return restaurants.stream()
                .map(restaurant -> toMenu(restaurant, vote != null && vote.getRestaurantId() == restaurant.id()))
                .collect(Collectors.toList());
    }

    public static Menu toMenu (Restaurant restaurant, boolean toVote){
        return new Menu(restaurant.id(), restaurant.getName(), restaurant.getDishes(), toVote);
    }
}
