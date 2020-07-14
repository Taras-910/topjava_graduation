package ru.javawebinar.topjava.repository.restaurant;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");
    public final CrudRestaurantRepository restaurantRepository;

    public DataJpaRestaurantRepository(CrudRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public boolean delete(int id) {
        return restaurantRepository.delete(id) != 0;
    }

    @Override
    public Restaurant get(int id) {
        return restaurantRepository.getOne(id);
    }

    @Override
    public List<Restaurant> getAll() { return restaurantRepository.findAll(SORT_NAME); }

    @Override
    public List<Restaurant> getWithDishes(LocalDate date) {
        return restaurantRepository.getWithDishes(date);
    }

    @Override
    public Restaurant getById(int restaurantId, LocalDate date) {
        return restaurantRepository.getById(restaurantId, date);
    }
}
