package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Dish;

import java.util.Date;
import java.util.List;

public interface DishRepository {

    Dish save(Dish dish, int restaurantId);

    boolean delete(int id, int restaurantId);

    Dish get(int id, int restaurantId);

    List<Dish> getByRestaurantAndDate(int restaurantId, Date localDate);

    List<Dish> getAll();
}
