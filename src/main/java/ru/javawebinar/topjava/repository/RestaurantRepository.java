package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Restaurant;

import java.util.Date;
import java.util.List;

public interface RestaurantRepository {
    // null if not found, when updated
    Restaurant save(Restaurant restaurant);

    // false if not found
    boolean delete(int id);

    // null if not found
    Restaurant getById(int id);

    List<Restaurant> getAll();

    List<Restaurant> getAllWithDishesOfDate(Date localDate);

    Restaurant getByIdWithDishesOfDate(int restaurantId, Date localDate);

    Restaurant getByName(String name);

    List<Restaurant> getAllWithDishes();
}
