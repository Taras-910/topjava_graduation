package ru.javawebinar.topjava.repository.dish;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.DishRepository;
import ru.javawebinar.topjava.repository.restaurant.CrudRestaurantRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaDishRepository implements DishRepository {
    private final CrudDishRepository dishRepository;
    private final CrudRestaurantRepository restaurantRepository;

    public DataJpaDishRepository(CrudDishRepository dishRepository, CrudRestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Dish save(Dish dish, int restaurantId) throws IllegalArgumentException{
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        dish.setRestaurant(restaurant);
        return dish.isNew() || get(dish.id(), restaurant.getId()) != null ? dishRepository.save(dish) : null; }

    @Override
    public List<Dish> saveAll(List<Dish> dishes, int restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            return null;
        }
        dishes.forEach(dish ->  dish.setRestaurant(restaurant));
        return dishRepository.saveAll(dishes);
    }

    @Override
    public boolean deleteListOfMenu(int restaurantId, LocalDate date) { return dishRepository.deleteListOfMenu(restaurantId, date) != 0; }

    @Override
    public boolean delete(int id, int restaurantId) {
        int exist = Optional.ofNullable(dishRepository.delete(id, restaurantId)).orElse(0);
        return exist != 0;
    }

    @Override
    public Dish get(int id, int restaurantId) {
        return Optional.ofNullable(dishRepository.get(id, restaurantId)).orElse(null);
    }

    @Override
    public List<Dish> getAllByRestaurant(int restaurantId) {
        List<Dish> dishes = Optional.ofNullable(dishRepository.getAll(restaurantId)).orElse(null);
        return dishes.isEmpty() ? null : dishes;
    }

    @Override
    public List<Dish> getByRestaurantAndDate(int restaurantId, LocalDate date) {
        List<Dish> dishes = Optional.ofNullable(dishRepository.getByRestaurantAndDate(restaurantId, date)).orElse(null);
        return dishes.isEmpty() ? null : dishes;
    }

    @Override
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }
}
