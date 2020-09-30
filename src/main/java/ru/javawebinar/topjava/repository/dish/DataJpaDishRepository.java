package ru.javawebinar.topjava.repository.dish;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.DishRepository;
import ru.javawebinar.topjava.repository.restaurant.CrudRestaurantRepository;

import java.util.Date;
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
    public Dish save(Dish dish, int restaurantId) {
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        dish.setRestaurant(restaurant);
        if(dish.isNew()){
            return dishRepository.save(dish);
        }
        Dish storedDish = Optional.ofNullable(dishRepository.get(dish.id(), restaurantId)).orElse(null);
        return storedDish != null ? dishRepository.save(dish) : null;
    }

    @Override
    public boolean delete(int id, int restaurantId) {
        return Optional.of(dishRepository.delete(id, restaurantId)).orElse(0) != 0;
    }

    @Override
    public Dish get(int id, int restaurantId) {
        return Optional.ofNullable(dishRepository.get(id, restaurantId)).orElse(null);
    }

    @Override
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @Override
    public List<Dish> getByRestaurantAndDate(int restaurantId, Date localDate) {
        List<Dish> dishes = Optional.ofNullable(dishRepository.getByRestaurantAndDate(restaurantId, localDate)).orElse(null);
        return dishes.isEmpty() ? null : dishes;
    }


}
