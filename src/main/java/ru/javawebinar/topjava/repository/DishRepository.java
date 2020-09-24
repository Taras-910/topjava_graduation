package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository {

    Dish save(Dish dish, int restaurantId);

    List<Dish> saveAll(List<Dish> dishes, int restaurantId);

    boolean deleteListOfMenu(int restaurantId, LocalDate date);

    boolean delete(int id, int restaurantId);

    Dish get(int id, int restaurantId);

    List<Dish> getByRestaurantAndDate(int restaurantId, LocalDate date);

    List<Dish> getAll();
}
