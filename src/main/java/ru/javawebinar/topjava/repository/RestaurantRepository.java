package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {
    // null if not found, when updated
    Restaurant save(Restaurant restaurant);

    // false if not found
    boolean delete(int id);

    // null if not found
    Restaurant get(int id);

    List<Restaurant> getAll();

    List<Restaurant> getWithDishes(LocalDate date);

    Restaurant getById(int restaurantId, LocalDate date);
}
