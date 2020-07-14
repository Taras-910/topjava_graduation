package ru.javawebinar.topjava.repository.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.DishRepository;
import ru.javawebinar.topjava.repository.restaurant.CrudRestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaDishRepository implements DishRepository {
    private static final Logger log = LoggerFactory.getLogger("");
    private final CrudDishRepository dishRepository;
    private final CrudRestaurantRepository restaurantRepository;

    public DataJpaDishRepository(CrudDishRepository dishRepository, CrudRestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Dish save(Dish dish, int restaurantId) {
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        dish.setRestaurant(restaurant);
        return dish.isNew() || get(dish.id()) != null ? dishRepository.save(dish) : null; }

    @Override
    public List<Dish> saveAll(List<Dish> dishes) {
        return dishRepository.saveAll(dishes);
    }

    @Override
    public boolean deleteAll(int restaurantId, LocalDate date) { return dishRepository.deleteAll(restaurantId, date) != 0; }

    @Override
    public boolean delete(int id, int restaurantId) {
        return dishRepository.delete(id, restaurantId) != 0;
    }

    @Override
    public Dish get(int id) {
        return dishRepository.findById(id).orElse(null);
    }

    @Override
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }
}
